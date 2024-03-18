package edu.agh.dean.classesverifierbe.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.dto.UserTagDTO;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.UserTag;
import edu.agh.dean.classesverifierbe.service.UserService;
import edu.agh.dean.classesverifierbe.service.UserTagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserTagController.class)
public class UserTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserTagService userTagService;


    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        given(userTagService.createTag(any(UserTagDTO.class))).willAnswer(invocation -> {
            UserTagDTO dto = invocation.getArgument(0);
            UserTag tag = new UserTag();
            tag.setName(dto.getName());
            return tag;
        });
    }

    @Test
    public void whenCreateTagWithValidData_thenRespondWithOk() throws Exception {
        UserTagDTO tagDTO = new UserTagDTO();
        tagDTO.setName("Java");
        tagDTO.setDescription("Java programming");

        mockMvc.perform(post("/user-tags/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagDTO)))
                .andExpect(status().isOk());
    }

}
