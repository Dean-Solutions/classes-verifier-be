package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.RO.UserRO;
import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.service.StudentService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;


    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody UserDTO userDto) throws UserAlreadyExistsException {
        User newUser = studentService.addUser(userDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRO> getUserById(@PathVariable Long id) throws UserNotFoundException {
        UserRO userRO = studentService.getUserById(id);
        return ResponseEntity.ok(userRO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserRO> deleteUserById(@PathVariable Long id) throws UserNotFoundException {
        UserRO userRO = studentService.removeUserById(id);
        return ResponseEntity.ok(userRO);
    }


    @GetMapping("/index/{indexNumber}")
    public ResponseEntity<User> getUserByIndexNumber(@PathVariable String indexNumber) throws UserNotFoundException {
        User user = studentService.findUserByIndexNumber(indexNumber);
        return ResponseEntity.ok(user);

    }



    @GetMapping
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


}