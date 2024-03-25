package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.RO.SubjectTagRO;
import edu.agh.dean.classesverifierbe.dto.SubjectTagDTO;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagNotFoundException;
import edu.agh.dean.classesverifierbe.service.SubjectTagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class SubjectTagController {

    private final SubjectTagService tagService;

    @Autowired
    public SubjectTagController(SubjectTagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<SubjectTagRO> createTag(@RequestBody @Valid SubjectTagDTO tagDto) throws SubjectTagAlreadyExistsException {
        SubjectTagRO createdTag = tagService.createTag(tagDto);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SubjectTagRO>> getAllTags() throws SubjectTagNotFoundException {
        List<SubjectTagRO> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<SubjectTagRO> getTag(@PathVariable Long tagId) throws SubjectTagNotFoundException {
        SubjectTagRO tag = tagService.getTag(tagId);
        return ResponseEntity.ok(tag);
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<SubjectTagRO> updateTag(@PathVariable Long tagId, @RequestBody @Valid SubjectTagDTO tagDto) throws SubjectTagNotFoundException {
        SubjectTagRO updatedTag = tagService.updateTag(tagId, tagDto);
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<SubjectTagRO> deleteTag(@PathVariable Long tagId) throws SubjectTagNotFoundException {
        SubjectTagRO deletedTag = tagService.deleteTag(tagId);
        return ResponseEntity.ok(deletedTag);
    }
}
