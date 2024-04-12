package edu.agh.dean.classesverifierbe.controller;


import edu.agh.dean.classesverifierbe.RO.EnrollmentRO;
import edu.agh.dean.classesverifierbe.dto.EnrollDTO;
import edu.agh.dean.classesverifierbe.dto.MultiEnrollDTO;
import edu.agh.dean.classesverifierbe.dto.EnrollForUserDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.service.AuthContextService;
import edu.agh.dean.classesverifierbe.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


import java.util.List;


@RestController
@RequestMapping("/enrollments")
@PreAuthorize("hasAnyRole('DEAN', 'STUDENT_REP', 'STUDENT')")
public class EnrollmentController {


    private final EnrollmentService enrollmentService;

    private final AuthContextService authContextService;
    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService, AuthContextService authContextService) {
        this.enrollmentService = enrollmentService;
        this.authContextService = authContextService;
    }


    @PutMapping("/accept/{enrollmentId}")
    public ResponseEntity<EnrollmentRO> confirmEnrollment(@PathVariable Long enrollmentId) throws EnrollmentNotFoundException{

        return ResponseEntity.ok(enrollmentService.acceptEnrollment(enrollmentId));
    }

    @PutMapping("/accept")
    public ResponseEntity<List<EnrollmentRO>> confirmEnrollments(@RequestBody List<Long> enrollmentIds) throws EnrollmentNotFoundException{
        return ResponseEntity.ok(enrollmentService.acceptEnrollments(enrollmentIds));
    }

    @GetMapping
    public ResponseEntity<Page<EnrollmentRO>> getAllEnrollments(Pageable pageable,
                                                @RequestParam(required = false) String indexNumber,
                                                @RequestParam(required = false) String subjectName,
                                                @RequestParam(required = false) Long semesterId,
                                                @RequestParam(required = false) String statuses,
                                                                @RequestParam(required = false) Long userId,
                                                                @RequestParam(required = false) Long subjectId)
            throws SemesterNotFoundException,NoPermissionException {
        User userFromToken = authContextService.getCurrentUser();
        if(userId != null && !userFromToken.getUserId().equals(userId)){
            throw new NoPermissionException("You can't get enrollments for other users");
        }
        Page<EnrollmentRO> enrollments = enrollmentService.getAllEnrollments(pageable, indexNumber, subjectName, semesterId, statuses,userId,subjectId);
        return ResponseEntity.ok(enrollments);
    }



    @PostMapping
    public ResponseEntity<Enrollment> assignEnrollmentForUser(@RequestBody @Valid EnrollDTO enrollDTO) throws UserNotFoundException,
            SubjectNotFoundException,
            SemesterNotFoundException,
            EnrollmentAlreadyExistException {
        Long userId = enrollDTO.getUserId();

        return ResponseEntity.ok(enrollmentService.assignEnrollmentForUser(enrollDTO));
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<EnrollmentRO> deleteEnrollment(@PathVariable Long enrollmentId) {
        EnrollmentRO enrollmentRO = enrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.ok(enrollmentRO);
    }

    @DeleteMapping
    public ResponseEntity<EnrollmentRO> deleteEnrollmentBySubjectUserSemester(@RequestBody @Valid EnrollDTO enrollDTO) throws UserNotFoundException, SemesterNotFoundException, SubjectNotFoundException{
        EnrollmentRO enrollmentRO = enrollmentService.deleteEnrollmentBySubjectUserSemester(enrollDTO);
        return ResponseEntity.ok(enrollmentRO);
    }

    @PostMapping("/multi")
    public ResponseEntity<List<EnrollmentRO>> assignEnrollmentForMultipleUsers(@RequestBody @Valid MultiEnrollDTO multiEnrollDTO) throws UserNotFoundException,
            SemesterNotFoundException,
            EnrollmentAlreadyExistException,
            SubjectNotFoundException {
        return ResponseEntity.ok(enrollmentService.assignEnrollmentsForMultipleUsers(multiEnrollDTO));
    }

    @PutMapping
    public ResponseEntity<EnrollmentRO> updateAssignStatusForUser(@RequestBody @Valid EnrollDTO enrollDTO) throws UserNotFoundException,
            SubjectNotFoundException,
            SemesterNotFoundException,
            EnrollmentNotFoundException {
        EnrollmentRO updatedEnroll = enrollmentService.updateEnrollmentForUser(enrollDTO);
        return ResponseEntity.ok(updatedEnroll);
    }

    @GetMapping("/user")
    public ResponseEntity<List<EnrollmentRO>> getEnrolledSubjectsByUserId(@RequestBody @Valid EnrollForUserDTO enrollForUserDTO) throws UserNotFoundException, SemesterNotFoundException {
        return ResponseEntity.ok(enrollmentService.getEnrolledSubjectsByUserId(enrollForUserDTO));
    }


    @GetMapping("/index")
    public ResponseEntity<List<EnrollmentRO>> getEnrolledSubjectsByUserIndex(@RequestBody @Valid EnrollForUserDTO enrollForUserDTO) throws UserNotFoundException, SemesterNotFoundException {
        return ResponseEntity.ok(enrollmentService.getEnrolledSubjectsByUserIndex(enrollForUserDTO));
    }
}
