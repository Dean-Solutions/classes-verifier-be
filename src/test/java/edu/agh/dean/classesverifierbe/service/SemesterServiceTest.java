package edu.agh.dean.classesverifierbe.service;


import edu.agh.dean.classesverifierbe.dto.SemesterDTO;
import edu.agh.dean.classesverifierbe.exceptions.SemesterAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.SemesterNotFoundException;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.enums.SemesterType;
import edu.agh.dean.classesverifierbe.repository.SemesterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SemesterServiceTest {

    @Mock
    private SemesterRepository semesterRepository;

    @InjectMocks
    private SemesterService semesterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSemesterByIdShouldReturnSemesterWhenFound() throws SemesterNotFoundException {
        Semester expectedSemester = new Semester(1L, SemesterType.WINTER, 2022, LocalDateTime.now());
        when(semesterRepository.findById(1L)).thenReturn(Optional.of(expectedSemester));

        Semester result = semesterService.getSemesterById(1L);

        assertEquals(expectedSemester, result);
        verify(semesterRepository).findById(1L);
    }

    @Test
    void getSemesterByIdShouldThrowWhenNotFound() {
        when(semesterRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(SemesterNotFoundException.class, () -> semesterService.getSemesterById(999L));
    }


    @Test
    void createSemesterShouldThrowWhenSemesterAlreadyExists() {
        SemesterDTO semesterDTO = new SemesterDTO();
        semesterDTO.setSemesterType(SemesterType.WINTER);
        semesterDTO.setYear(2022);
        semesterDTO.setDeadline(LocalDateTime.now());
        when(semesterRepository.findByYearAndSemesterType(2022, SemesterType.WINTER))
                .thenReturn(Optional.of(new Semester()));

        assertThrows(SemesterAlreadyExistsException.class, () -> semesterService.createSemester(semesterDTO));
    }

    @Test
    void createSemesterShouldCreateWhenNotExists() throws SemesterAlreadyExistsException {
        SemesterDTO semesterDTO = new SemesterDTO();
        semesterDTO.setSemesterType(SemesterType.WINTER);
        semesterDTO.setYear(2022);
        semesterDTO.setDeadline(LocalDateTime.now());
        when(semesterRepository.findByYearAndSemesterType(2022, SemesterType.WINTER))
                .thenReturn(Optional.empty());

        semesterService.createSemester(semesterDTO);

        verify(semesterRepository).save(any(Semester.class));
    }

}

