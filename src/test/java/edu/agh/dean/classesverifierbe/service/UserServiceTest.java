package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.dto.UserTagDTO;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.UserTag;
import edu.agh.dean.classesverifierbe.repository.UserRepository;
import edu.agh.dean.classesverifierbe.repository.UserTagRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTagRepository userTagRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private UserTagService userTagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void whenAddUser_thenUserShouldBeSavedWithGeneratedPassword() {
        UserDTO userDTO = UserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .indexNumber("123456")
                .email("john@example.com")
                .semester(1)
                .status(UserStatus.ACTIVE)
                .role(Role.STUDENT)
                .build();

        when(userRepository.existsByIndexNumber(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.addUser(userDTO);

        assertNotNull(result.getHashPassword(), "Password should be generated");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void whenAddExistingUser_thenThrowException() {
        UserDTO userDTO = UserDTO.builder().indexNumber("123456").build();

        when(userRepository.existsByIndexNumber("123456")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(userDTO);
        });

        assertEquals("User with this index number already exists", exception.getMessage());
    }


    @Test
    void whenAddTagToUser_thenTagShouldBeAdded() {
        String index = "123456";
        UserDTO userDTO = UserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .indexNumber(index)
                .email("john@example.com")
                .semester(1)
                .status(UserStatus.ACTIVE)
                .role(Role.STUDENT)
                .build();
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setIndexNumber(userDTO.getIndexNumber());
        user.setEmail(userDTO.getEmail());
        user.setSemester(userDTO.getSemester());
        user.setStatus(userDTO.getStatus());
        user.setRole(userDTO.getRole());
        user.setUserTags(new HashSet<>());

        when(userRepository.findByIndexNumber(any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        String tagName = "Spring";
        UserTagDTO userTagDTO = UserTagDTO.builder()
                .name(tagName)
                .description("Spring description")
                .build();
        UserTag tag = new UserTag();
        tag.setName(userTagDTO.getName());
        tag.setDescription(userTagDTO.getDescription());

        when(userTagRepository.findByName(any(String.class))).thenReturn(Optional.of(tag));
        when(userTagRepository.save(any(UserTag.class))).thenReturn(tag);
        User updatedUser = userService.addTagToUserByIndexAndTagName(index, tagName);
        assertNotNull(updatedUser);
        assertTrue(updatedUser.getUserTags().contains(tag), "Tag should be added to the user");
    }


    @Test
    void getStudentsByCriteriaTest() {
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User(); // Uzupełnij obiekt user odpowiednimi danymi
        Page<User> expectedPage = new PageImpl<>(Collections.singletonList(user));

        when(userRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(expectedPage);

        Page<User> resultPage = userService.getStudentsByCriteria(pageable, "Spring", "John", "Doe", "123456", 1);

        assertThat(resultPage.getContent()).hasSize(1);
        assertThat(resultPage.getContent().get(0)).isEqualToComparingFieldByField(user);

        verify(userRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }


}