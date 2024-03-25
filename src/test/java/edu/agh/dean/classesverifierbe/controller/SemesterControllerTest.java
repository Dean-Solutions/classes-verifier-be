package edu.agh.dean.classesverifierbe.controller;
import edu.agh.dean.classesverifierbe.RO.SubjectTagRO;
import edu.agh.dean.classesverifierbe.dto.SemesterDTO;
import edu.agh.dean.classesverifierbe.dto.SubjectTagDTO;
import edu.agh.dean.classesverifierbe.exceptions.SemesterNotFoundException;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.enums.SemesterType;
import edu.agh.dean.classesverifierbe.service.SemesterService;
import edu.agh.dean.classesverifierbe.service.SubjectTagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SemesterController.class)
class SemesterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SemesterService semesterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSemestersShouldReturnListOfSemesters() throws Exception {
        given(semesterService.getAllSemesters()).willReturn(List.of(new Semester(1L, SemesterType.WINTER, 2022, LocalDateTime.now())));

        mockMvc.perform(get("/semester"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].semesterType").value("WINTER"))
                .andExpect(jsonPath("$[0].year").value(2022));

        verify(semesterService).getAllSemesters();
    }

    @Test
    void getSemesterByYearAndTypeShouldReturnSemester() throws Exception {
        Integer year = 2022;
        String type = "WINTER";
        Semester foundSemester = new Semester(1L, SemesterType.valueOf(type), year, LocalDateTime.now());

        given(semesterService.getSemesterByYearAndType(year, SemesterType.valueOf(type))).willReturn(foundSemester);

        mockMvc.perform(get("/semester/year/{year}/type/{type}", year, type))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.semesterType").value("WINTER"))
                .andExpect(jsonPath("$.year").value(2022));

        verify(semesterService).getSemesterByYearAndType(year, SemesterType.valueOf(type));
    }

    @Test
    void updateCurrentSemesterShouldReturnNotFoundWhenCurrentSemesterDoesNotExist() throws Exception {
        SemesterDTO semesterDTO = new SemesterDTO();

        semesterDTO.setSemesterType(SemesterType.SUMMER);
        semesterDTO.setYear(2023);
        semesterDTO.setDeadline(LocalDateTime.now().plusMonths(6));

        given(semesterService.updateCurrentSemester(any(SemesterDTO.class))).willThrow(new SemesterNotFoundException("id"));

        mockMvc.perform(put("/semester/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"semesterType\":\"SUMMER\",\"year\":2023,\"deadline\":\"" + LocalDateTime.now().plusMonths(6).toString() + "\"}"))
                .andExpect(status().isNotFound());

        verify(semesterService).updateCurrentSemester(any(SemesterDTO.class));
    }




}

