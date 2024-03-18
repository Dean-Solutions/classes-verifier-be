package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.exceptions.UserAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.UserTagAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.UserTagNotFoundException;
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
    @ExceptionHandler({UserAlreadyExistsException.class, UserNotFoundException.class, UserTagAlreadyExistsException.class, UserTagNotFoundException.class})
    public ResponseEntity<?> handleCustomExceptions(Exception ex) {
        if(ex instanceof MethodArgumentNotValidException){
            return handleValidationExceptions((MethodArgumentNotValidException) ex);
        }
        else if (ex instanceof UserNotFoundException) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        else if(ex instanceof UserAlreadyExistsException){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
        else if(ex instanceof UserTagNotFoundException){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        else if(ex instanceof UserTagAlreadyExistsException){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTO userDto) {
        try {
            User newUser = userService.addUser(userDto);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
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
    public ResponseEntity<Page<User>> getStudents(Pageable pageable,
                                                  @RequestParam(required = false) String tag,
                                                  @RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String lastName,
                                                  @RequestParam(required = false) String indexNumber,
                                                  @RequestParam(required = false) Integer semester) {
        Page<User> users = userService.getStudentsByCriteria(pageable, tag, name, lastName, indexNumber, semester);
        if(users.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    @PostMapping("/assign-tag")
    public ResponseEntity<?> assignTagToUser(@RequestParam String index, @RequestParam String tagName) {
        try {
            User updatedUser = userService.addTagToUserByIndexAndTagName(index, tagName);
            return ResponseEntity.ok(updatedUser);

        } catch (UserNotFoundException | UserTagNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UserTagAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove-tag")
    public ResponseEntity<?> removeTagFromUser(@RequestParam String index, @RequestParam String tagName) {
        try {
            User updatedUser = userService.removeTagFromUserByIndexAndTagName(index, tagName);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException | UserTagNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}