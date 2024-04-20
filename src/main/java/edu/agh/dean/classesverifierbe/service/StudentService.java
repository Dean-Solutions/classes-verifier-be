package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.RO.UserRO;
import edu.agh.dean.classesverifierbe.auth.AuthenticationResponse;
import edu.agh.dean.classesverifierbe.auth.AuthenticationService;
import edu.agh.dean.classesverifierbe.auth.RegisterRequest;
import edu.agh.dean.classesverifierbe.dto.ChangePasswordDTO;
import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import edu.agh.dean.classesverifierbe.repository.SemesterRepository;
import edu.agh.dean.classesverifierbe.repository.UserRepository;
import edu.agh.dean.classesverifierbe.service.mail.MailHelperService;
import edu.agh.dean.classesverifierbe.specifications.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    public static final int INDEX_LEN = 6;

    private final UserRepository userRepository;

    private final SemesterRepository semesterRepository;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthContextService authContextService;
    private final AuthenticationService authenticationService;
    private final MailHelperService mailHelperService;
    public UserRO addUser(RegisterRequest request) throws UserAlreadyExistsException, InvalidIndexException, UserNotFoundException {

        validateStudentIndex(request.getIndexNumber());

        if (userRepository.existsByIndexNumber(request.getIndexNumber())) {
            throw new UserAlreadyExistsException("index number", request.getIndexNumber());
        }
        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException("email", request.getEmail());
        }
        AuthenticationResponse newUser = authenticationService.register(request);
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("email", request.getEmail()));
        mailHelperService.sendWelcomeEmail(user, newUser.getPassword());
        UserRO userRO = convertToUserRO(user);
        userRO.setHashPassword(null);
        return userRO;
    }


    //converter from UserDTO to User
    public User toUser(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setIndexNumber(userDTO.getIndexNumber());
        user.setEmail(userDTO.getEmail());
        user.setSemester(userDTO.getSemester());
        user.setStatus(userDTO.getStatus());
        user.setRole(userDTO.getRole());
        return user;
    }

    public User findUserByIndexNumber(String index) throws UserNotFoundException {
        return userRepository.findByIndexNumber(index).orElseThrow(() -> new UserNotFoundException("index number", index));
    }

    public UserRO getUserById(Long id) throws UserNotFoundException {
        User user = getRawUserById(id);
        return convertToUserRO(user);
    }

    public User getRawUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id", id.toString()));
    }

    public Page<UserRO> getStudentsByCriteria(Pageable pageable, String tag, String name, String lastName, String indexNumber, Integer semester, String status,Long semestrId) throws SemesterNotFoundException {
        Semester givenSemester = null;
        if(tag != null){ //we are looking for students from current semester that have given tag
            givenSemester = semesterRepository.findCurrentSemester().orElseThrow(() -> new SemesterNotFoundException("current semester not found"));
        }
        if(semestrId != null) { // if we specify semester we are looking for students from this semester (tags above will be from THIS semester if specified)
            givenSemester = semesterRepository.findById(semestrId).orElseThrow(() -> new SemesterNotFoundException("current semester not found"));
        }

        Page<User> users = userRepository.findAll(UserSpecifications.byCriteria(tag, name, lastName, indexNumber, semester,status,givenSemester), pageable);
        List<UserRO> userROs = users.getContent().stream()
                .map(this::convertToUserRO)
                .collect(Collectors.toList());
        return new PageImpl<>(userROs, pageable, users.getTotalElements());
    }


    private UserRO convertToUserRO(User user) {
        return modelMapper.map(user, UserRO.class);
    }


    public UserRO removeUserById(Long id) throws UserNotFoundException {
        User user = getRawUserById(id);
        userRepository.delete(user);
        return convertToUserRO(user);
    }

    private void validateStudentIndex(String index) throws InvalidIndexException {
        if (index == null || index.length() != INDEX_LEN || !index.matches("\\d+")) {
            throw new InvalidIndexException(index);
        }
    }
    public List<User> getUsersWithPendingEnrollment(Semester semester, Set<EnrollStatus> enrollStatuses) {
        return userRepository.findAll(
                UserSpecifications.
                        withEnrollmentStatus(enrollStatuses)
                        .and(UserSpecifications.withSemester(semester)));
    }

    public List<User> findAllDeans() {
        return userRepository.findAll(UserSpecifications.hasRoleDean());
    }

    public void changePassword(ChangePasswordDTO request, Principal connectedUser) throws IncorrectPasswordException, PasswordsDoNotMatchException {
        var user = authContextService.getUserFromPrincipal(connectedUser);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new PasswordsDoNotMatchException();
        }
        user.setHashPassword((passwordEncoder.encode(request.getNewPassword())));
        userRepository.save(user);
    }

    public void changePasswordForcefully(Long id, ChangePasswordDTO changePasswordDTO) throws UserNotFoundException,IncorrectPasswordException {
        var user = getRawUserById(id);
        var newPassword = changePasswordDTO.getNewPassword();
        if(newPassword == null ||newPassword.isEmpty()){
            throw new IncorrectPasswordException();
        }
        user.setHashPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
