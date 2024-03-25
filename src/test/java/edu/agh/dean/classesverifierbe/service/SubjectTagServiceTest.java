package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.RO.SubjectTagRO;
import edu.agh.dean.classesverifierbe.dto.SubjectTagDTO;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagNotFoundException;
import edu.agh.dean.classesverifierbe.model.SubjectTag;
import edu.agh.dean.classesverifierbe.repository.SubjectTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubjectTagServiceTest {

    @Mock
    private SubjectTagRepository subjectTagRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SubjectTagService subjectTagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTagSuccessful() throws SubjectTagAlreadyExistsException {
        SubjectTagDTO tagDto = new SubjectTagDTO();
        tagDto.setName("Math");
        tagDto.setDescription("Mathematics");

        SubjectTag tag = new SubjectTag();
        tag.setName("Math");
        tag.setDescription("Mathematics");

        SubjectTagRO tagRO = new SubjectTagRO();
        tagRO.setName(tag.getName());
        tagRO.setDescription(tag.getDescription());

        when(modelMapper.map(tagDto, SubjectTag.class)).thenReturn(tag);
        when(modelMapper.map(tag, SubjectTagRO.class)).thenReturn(tagRO);
        when(subjectTagRepository.save(tag)).thenReturn(tag);

        SubjectTagRO createdTag = subjectTagService.createTag(tagDto);


        verify(modelMapper).map(tagDto, SubjectTag.class);
        verify(subjectTagRepository).save(tag);
        verify(modelMapper).map(tag, SubjectTagRO.class);

        assertEquals("Math", createdTag.getName());
        assertEquals("Mathematics", createdTag.getDescription());
    }


    @Test
    void getTagNotFound() {
        when(subjectTagRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(SubjectTagNotFoundException.class, () -> {
            subjectTagService.getTag(1L);
        });
    }
}

