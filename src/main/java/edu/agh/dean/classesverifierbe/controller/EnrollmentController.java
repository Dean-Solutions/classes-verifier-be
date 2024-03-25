package edu.agh.dean.classesverifierbe.controller;


import edu.agh.dean.classesverifierbe.dto.EnrollDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;


    @PostMapping
    public ResponseEntity<?> assignEnrollmentForUser(@RequestBody @Valid EnrollDTO enrollDTO) throws UserNotFoundException,
            SubjectNotFoundException,
            SemesterNotFoundException,
            EnrollmentAlreadyExistException {
        return ResponseEntity.ok(enrollmentService.assignEnrollmentForUser(enrollDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAllEnrollmentForUser(@RequestBody @Valid EnrollDTO enrollDTO) throws UserNotFoundException {
        return ResponseEntity.ok(enrollmentService.getAllEnrollmentForUser(enrollDTO));
    }

}
