package edu.agh.dean.classesverifierbe.controller;


import edu.agh.dean.classesverifierbe.dto.EnrollDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }


    @PutMapping("/accept/{enrollmentId}")
    public ResponseEntity<Enrollment> confirmEnrollment(@PathVariable Long enrollmentId) throws EnrollmentNotFoundException{
        return ResponseEntity.ok(enrollmentService.acceptEnrollment(enrollmentId));
    }

    @PutMapping("/accept")
    public ResponseEntity<List<Enrollment>> confirmEnrollment(@RequestBody List<Long> enrollmentIds) throws EnrollmentNotFoundException{
        return ResponseEntity.ok(enrollmentService.acceptEnrollments(enrollmentIds));
    }

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    @PostMapping
    public ResponseEntity<Enrollment> assignEnrollmentForUser(@RequestBody @Valid EnrollDTO enrollDTO) throws UserNotFoundException,
            SubjectNotFoundException,
            SemesterNotFoundException,
            EnrollmentAlreadyExistException {
        return ResponseEntity.ok(enrollmentService.assignEnrollmentForUser(enrollDTO));
    }

    @PutMapping
    public ResponseEntity<Enrollment> updateAssignStatusForUser(@RequestBody @Valid EnrollDTO enrollDTO) throws UserNotFoundException,
            SubjectNotFoundException,
            SemesterNotFoundException,
            EnrollmentNotFoundException {
        Enrollment updatedEnroll = enrollmentService.updateEnrollmentForUser(enrollDTO);
        return ResponseEntity.ok(updatedEnroll);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Enrollment>> getEnrolledSubjectsByUserId(@PathVariable Long userId) throws UserNotFoundException {
        return ResponseEntity.ok(enrollmentService.getEnrolledSubjectsByUserId(userId));
    }


    @GetMapping("/index/{index}")
    public ResponseEntity<List<Enrollment>> getEnrolledSubjectsByUserIndex(@PathVariable String index) throws UserNotFoundException {
        return ResponseEntity.ok(enrollmentService.getEnrolledSubjectsByUserIndex(index));
    }




}
