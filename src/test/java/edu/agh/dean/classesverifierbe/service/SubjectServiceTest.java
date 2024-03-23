package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.exceptions.SubjectNotFoundException;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.repository.SubjectRepository;
import edu.agh.dean.classesverifierbe.repository.SubjectTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void createSubjectSuccessful() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");

        when(subjectRepository.findByName(anyString())).thenReturn(null);
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);

        subjectService.createSubject(subject);

        verify(subjectRepository).save(any(Subject.class));
    }

    @Test
    void getSubjectByIdNotFound() {
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(SubjectNotFoundException.class, () -> {
            subjectService.getSubjectById(1L);
        });
    }
}

