package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTO userDto) {
        try {
            User newUser = userService.addUser(userDto);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/index/{indexNumber}")
    public ResponseEntity<User> getUserByIndexNumber(@PathVariable String indexNumber) {
        Optional<User> user = userService.getUserByIndexNumber(indexNumber);
        return user
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public Page<User> getStudents(Pageable pageable,
                                  @RequestParam(required = false) String tag,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) String lastName,
                                  @RequestParam(required = false) String indexNumber,
                                  @RequestParam(required = false) Integer semester) {
        return userService.getStudentsByCriteria(pageable, tag, name, lastName, indexNumber, semester);
    }

    @PostMapping("/assign-tag")
    public ResponseEntity<?> assignTagToUser(@RequestParam String index, @RequestParam String tagName) {
        try {
            User updatedUser = userService.addTagToUserByIndexAndTagName(index, tagName);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove-tag")
    public ResponseEntity<?> removeTagFromUser(@RequestParam String index, @RequestParam String tagName) {
        try{
            User updatedUser = userService.removeTagFromUserByIndexAndTagName(index, tagName);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


}