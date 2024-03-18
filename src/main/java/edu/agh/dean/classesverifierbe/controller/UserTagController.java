package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.dto.UserTagDTO;
import edu.agh.dean.classesverifierbe.exceptions.UserTagAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.UserTagNotFoundException;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.UserTag;
import edu.agh.dean.classesverifierbe.service.UserTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-tags")
public class UserTagController {

    @Autowired
    private UserTagService userTagService;

    @ExceptionHandler({UserTagAlreadyExistsException.class, UserTagNotFoundException.class})
    public ResponseEntity<String> handleCustomExceptions(Exception ex) {
        if (ex instanceof UserTagNotFoundException) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        else if(ex instanceof UserTagAlreadyExistsException){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/")
    public ResponseEntity<?> createTag(@RequestBody UserTagDTO tagDTO) {
        try {
            UserTag createdTag = userTagService.createTag(tagDTO);
            return ResponseEntity.ok(createdTag);
        } catch (UserTagAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/")
    public ResponseEntity<Iterable<UserTag>> getAllTags() {
        Iterable<UserTag> tags = userTagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

}
