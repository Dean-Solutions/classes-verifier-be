package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.model.enums.UserStatus;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
}