package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.RO.SuccessRO;
import edu.agh.dean.classesverifierbe.RO.UserRO;
import edu.agh.dean.classesverifierbe.auth.RegisterRequest;
import edu.agh.dean.classesverifierbe.dto.ChangePasswordDTO;
import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.service.AuthContextService;
import edu.agh.dean.classesverifierbe.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/students")
@PreAuthorize("hasAnyRole('STUDENT', 'STUDENT_REP', 'DEAN')")
@RequiredArgsConstructor
public class StudentController {


    private final StudentService studentService;

    private final AuthContextService authContextService;

    @GetMapping("/my-profile")
    @Operation(summary = "STUDENT, STUDENT_REP, DEAN are allowed")
    public ResponseEntity<UserRO> getProfile() throws NoPermissionException {
        User user = authContextService.getCurrentUser();
        UserRO userRO = UserRO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .indexNumber(user.getIndexNumber())
                .semester(user.getSemester())
                .email(user.getEmail())
                .role(user.getRole())
                .eduPath(user.getEduPath())
                .status(user.getStatus())
                .eduPath(user.getEduPath())
                .build();
        return ResponseEntity.ok(userRO);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('user:create')")
    @Operation(summary = "DEAN is allowed")
    public ResponseEntity<UserRO> addUser(@Valid @RequestBody RegisterRequest request) throws UserAlreadyExistsException,
            InvalidIndexException, UserNotFoundException{
        UserRO newUser = studentService.addUser(request);
        newUser.setHashPassword(null);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    @Operation(summary = "DEAN,STUDENT_REP are allowed")
    public ResponseEntity<UserRO> getUserById(@PathVariable Long id) throws UserNotFoundException {
        UserRO userRO = studentService.getUserById(id);
        return ResponseEntity.ok(userRO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    @Operation(summary = "DEAN is allowed")
    public ResponseEntity<UserRO> deleteUserById(@PathVariable Long id) throws UserNotFoundException {
        UserRO userRO = studentService.removeUserById(id);
        return ResponseEntity.ok(userRO);
    }


    @GetMapping("/index/{indexNumber}")
    @PreAuthorize("hasAuthority('user:read')")
    @Operation(summary = "DEAN,STUDENT_REP are allowed")
    public ResponseEntity<User> getUserByIndexNumber(@PathVariable String indexNumber) throws UserNotFoundException {
        User user = studentService.findUserByIndexNumber(indexNumber);
        return ResponseEntity.ok(user);

    }



    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    @Operation(summary = "DEAN,STUDENT_REP are allowed")
    public ResponseEntity<Page<UserRO>> getStudents(Pageable pageable,
                                                    @RequestParam(required = false) String tag,
                                                    @RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String lastName,
                                                    @RequestParam(required = false) String indexNumber,
                                                    @RequestParam(required = false) Integer userSemester,
                                                    @RequestParam(required = false) String status,
                                                    @RequestParam(required = false) Long actualSemesterId){
        Page<UserRO> usersRO;
        try {
            usersRO = studentService.getStudentsByCriteria(pageable, tag, name, lastName, indexNumber, userSemester, status, actualSemesterId);
            usersRO.getContent().forEach(UserRO::hidePassword);
            return ResponseEntity.ok(usersRO);
        } catch(SemesterNotFoundException e){
            usersRO = new PageImpl<>(List.of(), pageable, 0);
            return ResponseEntity.ok(usersRO);
        }
    }

    @PreAuthorize("hasAuthority('user:change-password')")
    @PatchMapping("/change-password")
    @Operation(summary = "DEAN,STUDENT_REP,STUDENT are allowed")
    public ResponseEntity<SuccessRO> changePassword(
            @RequestBody ChangePasswordDTO request,
            Principal principal
    ) throws IncorrectPasswordException, PasswordsDoNotMatchException{
        studentService.changePassword(request,principal);

        return ResponseEntity.ok(SuccessRO.builder().success(true).build());
    }

    @PreAuthorize("hasAuthority('user:change-password-forcefully')")
    @PatchMapping("/{id}/force-change-password")
    @Operation(summary = "DEAN is allowed: only newPassword from changePasswordDTO is considered.")
    public ResponseEntity<SuccessRO> changePasswordByForce(
            @PathVariable Long id,
            @RequestBody ChangePasswordDTO changePasswordDTO
    ) throws IncorrectPasswordException, UserNotFoundException{
        studentService.changePasswordForcefully(id,changePasswordDTO);
        return ResponseEntity.ok(SuccessRO.builder().success(true).build());
    }


}