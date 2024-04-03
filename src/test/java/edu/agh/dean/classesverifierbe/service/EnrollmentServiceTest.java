package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.EnrollDTO;
import edu.agh.dean.classesverifierbe.exceptions.EnrollmentNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import edu.agh.dean.classesverifierbe.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private SemesterService semesterService;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private EnrollmentService enrollmentService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetEnrolledSubjectsByUserId_thenReturnsEmptyList() throws UserNotFoundException {

        Long userId = 1L;
        when(studentService.getRawUserById(userId)).thenReturn(new User());
        when(enrollmentRepository.findAllByEnrollStudent_UserId(userId)).thenReturn(Collections.emptyList());

        List<Enrollment> result = enrollmentService.getEnrolledSubjectsByUserId(userId);

        verify(studentService, times(1)).getRawUserById(userId);
        verify(enrollmentRepository, times(1)).findAllByEnrollStudent_UserId(userId);
        assertTrue(result.isEmpty(), "The result should be an empty list");
    }

    @Test
    void whenGetEnrolledSubjectsByUserId_thenReturnsNonEmptyList() throws UserNotFoundException {

        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);

        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(1L);
        enrollment.setEnrollStudent(user);


        List<Enrollment> expectedEnrollments = Collections.singletonList(enrollment);
        when(studentService.getRawUserById(userId)).thenReturn(user);
        when(enrollmentRepository.findAllByEnrollStudent_UserId(userId)).thenReturn(expectedEnrollments);


        List<Enrollment> actualEnrollments = enrollmentService.getEnrolledSubjectsByUserId(userId);


        verify(studentService, times(1)).getRawUserById(userId);
        verify(enrollmentRepository, times(1)).findAllByEnrollStudent_UserId(userId);
        assertFalse(actualEnrollments.isEmpty(), "The result should not be an empty list");
        assertEquals(expectedEnrollments.size(), actualEnrollments.size(), "The size of returned enrollments should match");
        assertEquals(expectedEnrollments.get(0).getEnrollmentId(), actualEnrollments.get(0).getEnrollmentId(), "The enrollment IDs should match");
    }

    @Test
    void whenUpdateEnrollmentForUser_thenUpdateStatus() throws Exception {

        EnrollDTO enrollDTO = EnrollDTO.builder()
                .userId(1L)
                .subjectId(1L)
                .enrollStatus(EnrollStatus.ACCEPTED)
                .build();

        enrollDTO.setEnrollStatus(EnrollStatus.ACCEPTED);

        User user = new User();
        user.setUserId(enrollDTO.getUserId());
        Subject subject = new Subject();
        subject.setSubjectId(enrollDTO.getSubjectId());
        Semester semester = new Semester();
        Enrollment existingEnrollment = new Enrollment();
        existingEnrollment.setEnrollStudent(user);
        existingEnrollment.setEnrollSubject(subject);
        existingEnrollment.setSemester(semester);
        existingEnrollment.setEnrollStatus(EnrollStatus.PENDING);

        when(studentService.getRawUserById(enrollDTO.getUserId())).thenReturn(user);
        when(subjectService.getSubjectById(enrollDTO.getSubjectId())).thenReturn(subject);
        when(semesterService.getCurrentSemester()).thenReturn(semester);
        when(enrollmentRepository.findEnrollmentByEnrollStudentAndEnrollSubjectAndSemester(user, subject, semester))
                .thenReturn(Optional.of(existingEnrollment));
        when(enrollmentRepository.save(existingEnrollment)).thenReturn(existingEnrollment);

        assertEquals(EnrollStatus.PENDING, existingEnrollment.getEnrollStatus());

        Enrollment updatedEnrollment = enrollmentService.updateEnrollmentForUser(enrollDTO);

        assertNotNull(updatedEnrollment);
        assertEquals(EnrollStatus.ACCEPTED, updatedEnrollment.getEnrollStatus());
        verify(enrollmentRepository).save(existingEnrollment);
    }


    @Test
    void whenAcceptEnrollments_thenStatusForEachEnrollmentIsACCEPTED() throws EnrollmentNotFoundException {

        List<Long> enrollmentIds = Arrays.asList(1L, 2L);
        List<Enrollment> enrollments = enrollmentIds.stream()
                .map(id -> {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setEnrollmentId(id);
                    enrollment.setEnrollStatus(EnrollStatus.PENDING);
                    return enrollment;
                })
                .collect(Collectors.toList());

        when(enrollmentRepository.findAllById(enrollmentIds)).thenReturn(enrollments);

        List<Enrollment> updatedEnrollments = enrollmentService.acceptEnrollments(enrollmentIds);

        for (Enrollment enrollment : updatedEnrollments) {
            assertEquals(EnrollStatus.ACCEPTED, enrollment.getEnrollStatus(), "Enrollment status should be ACCEPTED");
        }
        verify(enrollmentRepository).findAllById(enrollmentIds);
        verify(enrollmentRepository).saveAll(enrollments);
    }



}
