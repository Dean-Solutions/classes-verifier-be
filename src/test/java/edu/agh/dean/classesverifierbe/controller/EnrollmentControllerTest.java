//package edu.agh.dean.classesverifierbe.controller;
//
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import edu.agh.dean.classesverifierbe.RO.EnrollmentRO;
//import edu.agh.dean.classesverifierbe.dto.EnrollForUserDTO;
//import edu.agh.dean.classesverifierbe.model.Enrollment;
//import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
//import edu.agh.dean.classesverifierbe.service.EnrollmentService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(EnrollmentController.class)
//class EnrollmentControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private EnrollmentService enrollmentService;
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        this.objectMapper = new ObjectMapper();
//    }
//    @Test
//    void whenGetEnrolledSubjectsByUserIdAndSemester_thenReturnEmptyList() throws Exception {
//        Long userId = 1L;
//        Long semesterId = 1L;
//        Set<EnrollStatus> enrollStatuses = new HashSet<>();
//        enrollStatuses.add(EnrollStatus.ACCEPTED);
//        EnrollForUserDTO enrollForUserDTO = EnrollForUserDTO.builder()
//                .userId(1L)
//                .semesterId(1L)
//                .enrollStatuses(enrollStatuses)
//                .build();
//
//        given(enrollmentService.getEnrolledSubjectsByUserId(enrollForUserDTO)).willReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/enrollment/user")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(enrollForUserDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json("[]"));
//
//        verify(enrollmentService, times(1)).getEnrolledSubjectsByUserId(enrollForUserDTO);
//    }
//
//    @Test
//    void whenGetEnrolledSubjectsByUserIdAndSemester_thenReturnNonEmptyList() throws Exception {
//        Long userId = 1L;
//        Long semesterId = 1L;
//        EnrollmentRO enrollment = new EnrollmentRO();
//        enrollment.setEnrollmentId(1L);
//        Set<EnrollStatus> enrollStatuses = new HashSet<>();
//        enrollStatuses.add(EnrollStatus.ACCEPTED);
//        EnrollForUserDTO enrollForUserDTO = EnrollForUserDTO.builder()
//                .userId(1L)
//                .semesterId(1L)
//                .enrollStatuses(enrollStatuses)
//                .build();
//
//        List<EnrollmentRO> expectedEnrollments = Collections.singletonList(enrollment);
//        given(enrollmentService.getEnrolledSubjectsByUserId(enrollForUserDTO)).willReturn(expectedEnrollments);
//
//        mockMvc.perform(get("/enrollment/user")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(enrollForUserDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].enrollmentId").value(expectedEnrollments.get(0).getEnrollmentId()));
//
//        verify(enrollmentService, times(1)).getEnrolledSubjectsByUserId(enrollForUserDTO);
//    }
//
//
//
//
//    @Test
//    public void confirmEnrollmentSingleTest() throws Exception {
//
//        Long enrollmentId = 1L;
//        EnrollmentRO enrollment = new EnrollmentRO();
//        when(enrollmentService.acceptEnrollment(enrollmentId)).thenReturn(enrollment);
//
//        mockMvc.perform(put("/enrollment/accept/" + enrollmentId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//
//        verify(enrollmentService).acceptEnrollment(enrollmentId);
//    }
//
//    @Test
//    public void confirmEnrollmentMultipleTest() throws Exception {
//
//        List<Long> enrollmentIds = Arrays.asList(1L, 2L, 3L);
//        List<EnrollmentRO> enrollments = Arrays.asList(new EnrollmentRO(), new EnrollmentRO(), new EnrollmentRO());
//        when(enrollmentService.acceptEnrollments(enrollmentIds)).thenReturn(enrollments);
//
//        mockMvc.perform(put("/enrollment/accept")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(enrollmentIds)))
//                .andExpect(status().isOk());
//
//        verify(enrollmentService).acceptEnrollments(enrollmentIds);
//    }
//
//
//
//}
