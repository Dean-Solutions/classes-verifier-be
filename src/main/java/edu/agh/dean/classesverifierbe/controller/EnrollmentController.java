package edu.agh.dean.classesverifierbe.controller;


import edu.agh.dean.classesverifierbe.RO.EnrollmentRO;
import edu.agh.dean.classesverifierbe.dto.EnrollDTO;
import edu.agh.dean.classesverifierbe.dto.MultiEnrollDTO;
import edu.agh.dean.classesverifierbe.dto.EnrollForUserDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


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

//    @GetMapping
//    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
//        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
//    }

    @GetMapping
    public ResponseEntity<Page<EnrollmentRO>> getAllEnrollments(Pageable pageable,
                                                @RequestParam(required = false) String indexNumber,
                                                @RequestParam(required = false) String subjectName,
                                                @RequestParam(required = false) Long semesterId,
                                                @RequestParam(required = false) String statuses,
                                                                @RequestParam(required = false) Long userId,
                                                                @RequestParam(required = false) Long subjectId)
            throws SemesterNotFoundException {
        Page<EnrollmentRO> enrollments = enrollmentService.getAllEnrollments(pageable, indexNumber, subjectName, semesterId, statuses,userId,subjectId);
        return ResponseEntity.ok(enrollments);
    }

    @PostMapping
    public ResponseEntity<Enrollment> assignEnrollmentForUser(@RequestBody @Valid EnrollDTO enrollDTO) throws UserNotFoundException,
            SubjectNotFoundException,
            SemesterNotFoundException,
            EnrollmentAlreadyExistException {
        return ResponseEntity.ok(enrollmentService.assignEnrollmentForUser(enrollDTO));
    }

    @PostMapping("/multi")
    public ResponseEntity<List<Enrollment>> assignEnrollmentsForMultipleUsers(@RequestBody @Valid MultiEnrollDTO multiEnrollDTO) throws UserNotFoundException,
            SemesterNotFoundException,
            EnrollmentAlreadyExistException,
            SubjectNotFoundException {
        return ResponseEntity.ok(enrollmentService.assignEnrollmentsForMultipleUsers(multiEnrollDTO));
    }

    @PutMapping
    public ResponseEntity<Enrollment> updateAssignStatusForUser(@RequestBody @Valid EnrollDTO enrollDTO) throws UserNotFoundException,
            SubjectNotFoundException,
            SemesterNotFoundException,
            EnrollmentNotFoundException {
        Enrollment updatedEnroll = enrollmentService.updateEnrollmentForUser(enrollDTO);
        return ResponseEntity.ok(updatedEnroll);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Enrollment>> getEnrolledSubjectsByUserId(@RequestBody @Valid EnrollForUserDTO enrollForUserDTO) throws UserNotFoundException, SemesterNotFoundException {
        return ResponseEntity.ok(enrollmentService.getEnrolledSubjectsByUserId(enrollForUserDTO));
    }


    @GetMapping("/index")
    public ResponseEntity<List<Enrollment>> getEnrolledSubjectsByUserIndex(@RequestBody @Valid EnrollForUserDTO enrollForUserDTO) throws UserNotFoundException, SemesterNotFoundException {
        return ResponseEntity.ok(enrollmentService.getEnrolledSubjectsByUserIndex(enrollForUserDTO));
    }
}
