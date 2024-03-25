package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.RO.SubjectTagRO;
import edu.agh.dean.classesverifierbe.dto.SubjectTagDTO;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagNotFoundException;
import edu.agh.dean.classesverifierbe.service.SubjectTagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectTagController.class)
class SubjectTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectTagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTagShouldReturnCreatedTag() throws Exception {
        SubjectTagDTO tagDto = new SubjectTagDTO();
        tagDto.setName("Math");
        tagDto.setDescription("Mathematics Description");

        SubjectTagRO createdTag = new SubjectTagRO(1, "Math", "Mathematics Description");

        given(tagService.createTag(any(SubjectTagDTO.class))).willReturn(createdTag);

        mockMvc.perform(post("/tag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Math\",\"description\":\"Mathematics Description\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Math"));

        verify(tagService).createTag(any(SubjectTagDTO.class));
    }

    @Test
    void getTagShouldReturnNotFoundWhenTagDoesNotExist() throws Exception {
        Long tagId = 999L;
        given(tagService.getTag(tagId)).willThrow(new SubjectTagNotFoundException());

        mockMvc.perform(get("/tag/{tagId}", tagId))
                .andExpect(status().isNotFound());

        verify(tagService).getTag(tagId);
    }

    @Test
    void updateTagShouldReturnUpdatedTag() throws Exception {
        Long tagId = 1L;
        int tagIntId = 1;
        SubjectTagDTO tagDto = new SubjectTagDTO();
        tagDto.setName("UpdatedName");
        tagDto.setDescription("Updated Description");
        SubjectTagRO updatedTag = new SubjectTagRO();
        updatedTag.setName("UpdatedName");
        updatedTag.setDescription("Updated Description");
        updatedTag.setSubjectTagId(tagIntId);
        given(tagService.updateTag(eq(tagId), any(SubjectTagDTO.class))).willReturn(updatedTag);

        mockMvc.perform(put("/tag/{tagId}", tagId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedName\",\"description\":\"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(tagService).updateTag(eq(tagId), any(SubjectTagDTO.class));
    }



}
