package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.repository.UserRepository;
import edu.agh.dean.classesverifierbe.specifications.UserSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User addUser(UserDTO userDTO) {

        if (userRepository.existsByIndexNumber(userDTO.getIndexNumber())) {
            throw new IllegalArgumentException("User with this index number already exists");
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


    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByIndexNumber(String indexNumber) {
        return userRepository.findByIndexNumber(indexNumber);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> getStudentsByCriteria(Pageable pageable, String tag, String name, String lastName, String indexNumber, Integer semester) {
        return userRepository.findAll(UserSpecifications.byCriteria(tag, name, lastName, indexNumber, semester), pageable);
    }
}
