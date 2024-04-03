package edu.agh.dean.classesverifierbe.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import edu.agh.dean.classesverifierbe.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.objectMapper = new ObjectMapper();
    }
    @Test
    void whenGetEnrolledSubjectsByUserId_thenReturnEmptyList() throws Exception {

        Long userId = 1L;
        given(enrollmentService.getEnrolledSubjectsByUserId(userId)).willReturn(Collections.emptyList());


        mockMvc.perform(get("/enrollment/user/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(enrollmentService, times(1)).getEnrolledSubjectsByUserId(userId);
    }

    @Test
    void whenGetEnrolledSubjectsByUserId_thenReturnNonEmptyList() throws Exception {

        Long userId = 1L;
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(1L);


        List<Enrollment> expectedEnrollments = Collections.singletonList(enrollment);
        given(enrollmentService.getEnrolledSubjectsByUserId(userId)).willReturn(expectedEnrollments);


        mockMvc.perform(get("/enrollment/user/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].enrollmentId").value(expectedEnrollments.get(0).getEnrollmentId()));

        verify(enrollmentService, times(1)).getEnrolledSubjectsByUserId(userId);
    }


    @Test
    public void confirmEnrollmentSingleTest() throws Exception {

        Long enrollmentId = 1L;
        Enrollment enrollment = new Enrollment();
        when(enrollmentService.acceptEnrollment(enrollmentId)).thenReturn(enrollment);

        mockMvc.perform(put("/enrollment/accept/" + enrollmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(enrollmentService).acceptEnrollment(enrollmentId);
    }

    @Test
    public void confirmEnrollmentMultipleTest() throws Exception {

        List<Long> enrollmentIds = Arrays.asList(1L, 2L, 3L);
        List<Enrollment> enrollments = Arrays.asList(new Enrollment(), new Enrollment(), new Enrollment());
        when(enrollmentService.acceptEnrollments(enrollmentIds)).thenReturn(enrollments);

        mockMvc.perform(put("/enrollment/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollmentIds)))
                .andExpect(status().isOk());

        verify(enrollmentService).acceptEnrollments(enrollmentIds);
    }



}
