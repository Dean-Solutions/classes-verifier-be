package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.RO.UserRO;
import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.exceptions.UserAlreadyExistsException;
import edu.agh.dean.classesverifierbe.model.*;
import edu.agh.dean.classesverifierbe.model.enums.SemesterType;
import edu.agh.dean.classesverifierbe.repository.SemesterRepository;
import edu.agh.dean.classesverifierbe.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class StudentServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SemesterRepository semesterRepository;

    @Mock
    private ModelMapper modelMapper;


    @InjectMocks
    private StudentService studentService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void whenAddUser_thenUserShouldBeSavedWithGeneratedPassword() throws Exception{
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
            User result = studentService.addUser(userDTO);
            assertNotNull(result.getHashPassword(), "Password should be generated");
            verify(userRepository).save(any(User.class));

    }

    @Test
    void whenAddExistingUser_thenThrowException() {
        UserDTO userDTO = UserDTO.builder().indexNumber("123456").build();
        when(userRepository.existsByIndexNumber("123456")).thenReturn(true);

        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
            studentService.addUser(userDTO);
        });
        assertEquals("User with index number : " + 123456 + " already exists", exception.getMessage());
    }


    @Test
    void testFilteringByTags() {

        // Setup test data
        Semester currentSemester = new Semester();
        currentSemester.setSemesterId(1L);
        currentSemester.setSemesterType(SemesterType.WINTER);
        currentSemester.setYear(2023);
        currentSemester.setDeadline(LocalDateTime.parse("2024-01-01T00:00:00"));

        SubjectTag tag1 = new SubjectTag();
        tag1.setSubjectTagId(1);
        tag1.setName("algo");

        SubjectTag tag2 = new SubjectTag();
        tag2.setSubjectTagId(2);
        tag2.setName("semester1");

        Subject subject = new Subject();
        subject.setSubjectId(1L);
        subject.setName("Math");
        subject.setDescription("Mathematics");
        subject.setSubjectTags(new HashSet<>(Arrays.asList(tag1, tag2)));

        User user = new User();
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setIndexNumber("123456");
        user.setEmail("john.doe@op.pl");

        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(1L);
        enrollment.setSemester(currentSemester);
        enrollment.setEnrollSubject(subject);
        user.setEnrollments(new HashSet<>(Collections.singletonList(enrollment)));

        // Mocking repository calls
        when(semesterRepository.findCurrentSemester()).thenReturn(Optional.of(currentSemester));
        when(userRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(user)));

        Pageable pageable = PageRequest.of(0, 10);

        Page<UserRO> filteredUsers = studentService.getStudentsByCriteria(pageable, "algo", "name", "lastName", "indexNumber", 1, "ACTIVE");


        assertFalse(filteredUsers.isEmpty());
        assertEquals(1, filteredUsers.getContent().size());
    }


    @Test
    void getStudentsByCriteriaTest() {
       User user1 = User.builder().userId(1L).firstName("John").lastName("Doe")
               .indexNumber("123456").email("john.doe@op.pl").semester(1)
               .status(UserStatus.ACTIVE).role(Role.STUDENT).build();

       UserRO user1RO = UserRO.builder().userId(1L).firstName("John").lastName("Doe")
               .indexNumber("123456").email("john.doe@op.pl").status(UserStatus.ACTIVE)
                .role(Role.STUDENT).build();

        Pageable pageable = Pageable.unpaged();
        when(userRepository.findAll(any(), eq(pageable))).thenReturn(new PageImpl<>(Arrays.asList(user1)));

        when(modelMapper.map(any(User.class), eq(UserRO.class))).thenReturn(user1RO);

        studentService.getStudentsByCriteria(pageable, null, "john", null, null, null, "ACTIVE");


        verify(userRepository).findAll(any(), eq(pageable));
        verify(modelMapper, times(1)).map(any(User.class), eq(UserRO.class));
    }






}