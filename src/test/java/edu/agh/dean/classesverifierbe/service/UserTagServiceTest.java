//package edu.agh.dean.classesverifierbe.service;
//
//import edu.agh.dean.classesverifierbe.dto.UserTagDTO;
//import edu.agh.dean.classesverifierbe.exceptions.UserTagAlreadyExistsException;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class UserTagServiceTest {
//
//    @Mock
//    private UserTagRepository userTagRepository;
//
//    @InjectMocks
//    private UserTagService userTagService;
//
//    @Test
//    void whenCreateTag_thenTagShouldBeSaved() throws Exception {
//        UserTagDTO tagDTO = new UserTagDTO();
//        tagDTO.setName("Java");
//        tagDTO.setDescription("Java programming");
//
//        when(userTagRepository.findByName(anyString())).thenReturn(Optional.empty());
//        when(userTagRepository.save(any(UserTag.class))).thenAnswer(i -> i.getArguments()[0]);
//
//        UserTag result = userTagService.createTag(tagDTO);
//
//        assertNotNull(result);
//        assertEquals("Java", result.getName());
//        verify(userTagRepository).save(any(UserTag.class));
//    }
//
//    @Test
//    void whenCreateTagWithNameThatExists_thenThrowException() {
//        UserTagDTO tagDTO = new UserTagDTO();
//        tagDTO.setName("Java");
//
//        when(userTagRepository.findByName("Java")).thenReturn(Optional.of(new UserTag()));
//
//        Exception exception = assertThrows(UserTagAlreadyExistsException.class, () -> userTagService.createTag(tagDTO));
//        //UserTag with "+ attribute + " : " + value + " already exists
//        assertEquals("UserTag with name : Java already exists", exception.getMessage());
//    }
//}
