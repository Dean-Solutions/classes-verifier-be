package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.exceptions.UserAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.UserTagAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.UserTagNotFoundException;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.repository.SemesterRepository;
import edu.agh.dean.classesverifierbe.repository.UserRepository;
import edu.agh.dean.classesverifierbe.specifications.UserSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    public User addUser(UserDTO userDTO) throws UserAlreadyExistsException{

        if (userRepository.existsByIndexNumber(userDTO.getIndexNumber())) {
            throw new UserAlreadyExistsException("index number", userDTO.getIndexNumber());
        }
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new UserAlreadyExistsException("email", userDTO.getEmail());
        }
        User user = toUser(userDTO);

        String newPassword = UUID.randomUUID().toString();
        user.setHashPassword(newPassword);

        return userRepository.save(user);
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

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByIndexNumber(String indexNumber) {
        return userRepository.findByIndexNumber(indexNumber);
    }


    public Page<User> getStudentsByCriteria(Pageable pageable, String tag, String name, String lastName, String indexNumber, Integer semester, String status) {

        Semester currentSemester = semesterRepository.findCurrentSemester().orElseThrow(() -> new IllegalStateException("No current semester found"));

        return userRepository.findAll(UserSpecifications.byCriteria(tag, name, lastName, indexNumber, semester,status,currentSemester), pageable);
    }



}
