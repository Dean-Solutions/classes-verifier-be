package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User addUser(UserDTO userDTO) {
        //checking if user with this index number already exists or with email
        if (userRepository.existsByIndexNumber(userDTO.getIndexNumber())) {
            throw new IllegalArgumentException("User with this index number already exists");
        }
        User user = toUser(userDTO);
        return userRepository.save(user);
    }


    //converter from UserDTO to User
    public User toUser(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setIndexNumber(userDTO.getIndexNumber());
        user.setEmail(userDTO.getEmail());
        user.setHashPassword(userDTO.getPassword());
        user.setSemester(userDTO.getSemester());
        user.setStatus(userDTO.getStatus());
        user.setRole(userDTO.getRole());
        return user;
    }


    public Object getAllUsers() {
        return userRepository.findAll();
    }
}
