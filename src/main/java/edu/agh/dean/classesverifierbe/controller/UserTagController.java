package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.dto.UserTagDTO;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.UserTag;
import edu.agh.dean.classesverifierbe.service.UserTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-tags")
public class UserTagController {

    @Autowired
    private UserTagService userTagService;

    @PostMapping("/")
    public ResponseEntity<UserTag> createTag(@RequestBody UserTagDTO tagDTO) {
        UserTag createdTag = userTagService.createTag(tagDTO);
        return ResponseEntity.ok(createdTag);
    }



    @GetMapping("/")
    public ResponseEntity<Iterable<UserTag>> getAllTags() {
        Iterable<UserTag> tags = userTagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

}
