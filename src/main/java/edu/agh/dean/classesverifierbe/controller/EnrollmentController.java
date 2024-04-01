package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.exceptions.SemesterNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getEnrolledSubjectsByUserId(@PathVariable Long userId) {
        try {
            List<Enrollment> enrolledSubjects = enrollmentService.getEnrolledSubjectsByUserId(userId);
            return ResponseEntity.ok(enrolledSubjects);
        } catch (UserNotFoundException | SemesterNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/user/index/{index}")
    public ResponseEntity<?> getEnrolledSubjectsByUserIndex(@PathVariable String index) {
        try {
            List<Enrollment> enrolledSubjects = enrollmentService.getEnrolledSubjectsByUserIndex(index);
            return ResponseEntity.ok(enrolledSubjects);
        } catch (UserNotFoundException | SemesterNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}


