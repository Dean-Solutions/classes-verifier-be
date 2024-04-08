package edu.agh.dean.classesverifierbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.agh.dean.classesverifierbe.dto.RequestDTO;
import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(RequestControllerTest.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    RequestService requestService;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    void setUp() throws Exception{

        given(requestService.addRequest(any(RequestDTO.class))).willAnswer(invocation -> {
            RequestDTO dto = invocation.getArgument(0);
            Request request = new Request();

            return request;
        });
    }
}
