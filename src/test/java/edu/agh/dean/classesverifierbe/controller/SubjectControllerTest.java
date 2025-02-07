package edu.agh.dean.classesverifierbe.controller;


import edu.agh.dean.classesverifierbe.RO.UserRO;
import edu.agh.dean.classesverifierbe.exceptions.SubjectNotFoundException;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    @MockBean
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSubjectsShouldReturnPageOfSubjects() throws Exception {
        Subject subject = new Subject();
        subject.setSubjectId(1L);
        subject.setName("Algebra");
        subject.setDescription("Detailed description of Algebra");
        subject.setSemester(1); // Example semester

        Page<Subject> pageOfSubjects = new PageImpl<>(Collections.singletonList(subject));

        // Notice the inclusion of the semester parameter in the service call
        when(subjectService.getAllSubjects(eq("Math"), eq("Algebra"), eq(1), any(Pageable.class)))
                .thenReturn(pageOfSubjects);

        mockMvc.perform(get("/subjects")
                        .param("tags", "Math")
                        .param("name", "Algebra")
                        .param("semester", "1")) // Including semester in the request
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].name").value("Algebra"))
                .andExpect(jsonPath("$.content[0].description").value("Detailed description of Algebra"))
                .andExpect(jsonPath("$.content[0].semester").value(1));

        verify(subjectService).getAllSubjects(eq("Math"), eq("Algebra"), eq(1), any(Pageable.class));
    }

    @Test
    void getSubjectsBySemesterShouldReturnSubjects() throws Exception {
        List<Subject> subjects = List.of(
                new Subject(1L, "Math 101", "Basic Math", 1, null, null),
                new Subject(2L, "Math 102", "Advanced Math", 1, null, null)
        );

        when(subjectService.getAllSubjectsBySemester(1)).thenReturn(subjects);

        mockMvc.perform(get("/subjects/semester/{semester}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Math 101"))
                .andExpect(jsonPath("$[1].name").value("Math 102"));

        verify(subjectService).getAllSubjectsBySemester(1);
    }




    @Test
    void getUsersEnrolledInSubjectForSemesterShouldReturnListOfUsers() throws Exception {

        Long subjectId = 1L;
        Long semesterId = 1L;

        List<UserRO> mockUsers = new ArrayList<>();
        UserRO user = UserRO.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .indexNumber("123456")
                .build();
        mockUsers.add(user);

        when(subjectService.getUsersEnrolledInSubjectForSemester(subjectId, semesterId)).thenReturn(mockUsers);


        mockMvc.perform(get("/subjects/{subjectId}/users", subjectId)
                        .param("semesterId", semesterId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId").value(mockUsers.get(0).getUserId()))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].indexNumber").value("123456"));


        verify(subjectService).getUsersEnrolledInSubjectForSemester(subjectId, semesterId);
    }

    @Test
    void getUsersEnrolledInSubjectForSemesterShouldReturnNotFoundWhenSubjectNotFound() throws Exception {

        Long subjectId = 999L;
        Long semesterId = 1L;

        when(subjectService.getUsersEnrolledInSubjectForSemester(subjectId, semesterId))
                .thenThrow(new SubjectNotFoundException());

        mockMvc.perform(get("/subjects/{subjectId}/users", subjectId)
                        .param("semesterId", semesterId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(subjectService).getUsersEnrolledInSubjectForSemester(subjectId, semesterId);
    }





}

