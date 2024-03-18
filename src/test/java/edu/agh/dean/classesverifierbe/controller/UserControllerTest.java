package edu.agh.dean.classesverifierbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.UserTag;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.model.enums.UserStatus;
import edu.agh.dean.classesverifierbe.service.UserService;
import edu.agh.dean.classesverifierbe.service.UserTagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserTagService userTagService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception{

        given(userService.addUser(any(UserDTO.class))).willAnswer(invocation -> {
            UserDTO dto = invocation.getArgument(0);
            User user = new User();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());

            return user;
        });

    }

    @Test
    public void whenAddUserWithValidData_thenRespondWithCreated() throws Exception {
        UserDTO userDTO = UserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .indexNumber("123456")
                .email("john@example.com")
                .semester(1)
                .status(UserStatus.ACTIVE)
                .role(Role.STUDENT)
                .build();

        mockMvc.perform(post("/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenAddUserWithInvalidData_thenRespondWithBadRequest() throws Exception {
        String invalidUserJson = "{}";

        mockMvc.perform(post("/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());
    }




}
