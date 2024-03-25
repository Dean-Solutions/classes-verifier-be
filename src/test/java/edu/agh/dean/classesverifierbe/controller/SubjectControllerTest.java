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

        Page<Subject> pageOfSubjects = new PageImpl<>(Collections.singletonList(subject));

        when(subjectService.getAllSubjects(eq("Math"), eq("Algebra"), any(Pageable.class)))
                .thenReturn(pageOfSubjects);

        mockMvc.perform(get("/subject")
                        .param("tags", "Math")
                        .param("name", "Algebra"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].name").value("Algebra"))
                .andExpect(jsonPath("$.content[0].description").value("Detailed description of Algebra"));

        verify(subjectService).getAllSubjects(eq("Math"), eq("Algebra"), any(Pageable.class));
    }


    @Test
    void addTagToSubjectShouldReturnOkWhenSuccess() throws Exception {

        Subject subject = new Subject();
        when(subjectService.addTagToSubject(anyLong(), anyLong())).thenReturn(subject);

        mockMvc.perform(post("/subject/1/tags/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(subjectService).addTagToSubject(1L, 1L);
    }

    @Test
    void addTagToSubjectShouldReturnNotFoundWhenSubjectNotFound() throws Exception {

        when(subjectService.addTagToSubject(anyLong(), anyLong())).thenThrow(new SubjectNotFoundException("subjectId"));

        mockMvc.perform(post("/subject/999/tags/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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


        mockMvc.perform(get("/subject/{subjectId}/users", subjectId)
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

        mockMvc.perform(get("/subject/{subjectId}/users", subjectId)
                        .param("semesterId", semesterId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(subjectService).getUsersEnrolledInSubjectForSemester(subjectId, semesterId);
    }





}

