package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.RO.UserRO;
import edu.agh.dean.classesverifierbe.exceptions.SemesterNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.SubjectNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagAlreadyExistsException;
import edu.agh.dean.classesverifierbe.model.*;
import edu.agh.dean.classesverifierbe.model.enums.SemesterType;
import edu.agh.dean.classesverifierbe.repository.SubjectRepository;
import edu.agh.dean.classesverifierbe.repository.SubjectTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private SubjectTagRepository subjectTagRepository;

    @InjectMocks
    private SubjectService subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void createSubjectSuccessful() throws Exception {
//        Subject subject = new Subject();
//        subject.setName("Math");
//
//        when(subjectRepository.findByName(anyString())).thenReturn(null);
//        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);
//
//        subjectService.createSubject(subject);
//
//        verify(subjectRepository).save(any(Subject.class));
//    }

    @Test
    void getSubjectByIdNotFound() {
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(SubjectNotFoundException.class, () -> {
            subjectService.getSubjectById(1L);
        });
    }


    @Test
    void getUsersEnrolledInSubjectForSemesterShouldReturnUsers() throws SubjectNotFoundException, SemesterNotFoundException {
        Long subjectId = 1L;
        Long semesterId = 1L;

        Subject subject = mock(Subject.class);
        User user = User.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .indexNumber("123456")
                .build();
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollStudent(user);
        enrollment.setSemester(new Semester(semesterId, SemesterType.WINTER, 2021, LocalDateTime.now()));
        Set<Enrollment> enrollments = new HashSet<>();
        enrollments.add(enrollment);
        when(subject.getEnrollments()).thenReturn(enrollments);

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));

        List<UserRO> result = subjectService.getUsersEnrolledInSubjectForSemester(subjectId, semesterId);


        verify(subjectRepository).findById(subjectId);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
    }



    @Test
    void shouldFilterSubjectsByTagsAndName() throws SubjectNotFoundException {

        PageRequest pageable = PageRequest.of(0, 10);
        String tags = "Math,Science";
        String name = "Algebra";

        Subject subject = new Subject();
        subject.setName("Algebra 101");


        when(subjectRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(Collections.singletonList(subject)));


        Page<Subject> result = subjectService.getAllSubjects(tags, name, pageable);

        verify(subjectRepository).findAll(any(Specification.class), eq(pageable));
        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
        assertEquals("Algebra 101", result.getContent().get(0).getName());
    }


}





