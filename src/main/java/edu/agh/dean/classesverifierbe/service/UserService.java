package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.UserTag;
import edu.agh.dean.classesverifierbe.repository.UserRepository;
import edu.agh.dean.classesverifierbe.repository.UserTagRepository;
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
    @Autowired
    private UserTagRepository userTagRepository;

    public User addUser(UserDTO userDTO) {

        if (userRepository.existsByIndexNumber(userDTO.getIndexNumber())) {
            throw new IllegalArgumentException("User with this index number already exists");
        }
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new IllegalArgumentException("User with this email already exists");
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

    public User addTagToUserByIndexAndTagName(String index, String tagName) {
        User user = userRepository.findByIndexNumber(index)
                .orElseThrow(() -> new RuntimeException("User not found with index: " + index));
        UserTag tag = userTagRepository.findByName(tagName)
                .orElseThrow(() -> new RuntimeException("Tag not found with name: " + tagName));
        if (user.getUserTags().contains(tag)) {
            throw new RuntimeException("User already has this tag");
        }
        user.getUserTags().add(tag);
        return userRepository.save(user);
    }
    public User removeTagFromUserByIndexAndTagName(String index, String tagName) {
        User user = userRepository.findByIndexNumber(index)
                .orElseThrow(() -> new RuntimeException("User not found with index: " + index));
        UserTag tag = userTagRepository.findByName(tagName)
                .orElseThrow(() -> new RuntimeException("Tag not found with name: " + tagName));
        if (!user.getUserTags().contains(tag)) {
            throw new RuntimeException("User does not have this tag");
        }
        user.getUserTags().remove(tag);
        return userRepository.save(user);
    }


    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByIndexNumber(String indexNumber) {
        return userRepository.findByIndexNumber(indexNumber);
    }



    public Page<User> getStudentsByCriteria(Pageable pageable, String tag, String name, String lastName, String indexNumber, Integer semester) {
        return userRepository.findAll(UserSpecifications.byCriteria(tag, name, lastName, indexNumber, semester), pageable);
    }
}
