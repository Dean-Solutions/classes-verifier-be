package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService studentService;


    @PostMapping("/")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTO userDto) {
        try {
            User newUser = studentService.addUser(userDto);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = studentService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = studentService.getUserById(id);
        return user
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/index/{indexNumber}")
    public ResponseEntity<User> getUserByIndexNumber(@PathVariable String indexNumber) {
        Optional<User> user = studentService.getUserByIndexNumber(indexNumber);
        return user
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}