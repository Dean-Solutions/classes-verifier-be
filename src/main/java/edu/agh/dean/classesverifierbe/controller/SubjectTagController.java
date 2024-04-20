package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.RO.SubjectTagRO;
import edu.agh.dean.classesverifierbe.dto.SubjectTagDTO;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagNotFoundException;
import edu.agh.dean.classesverifierbe.service.SubjectTagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@PreAuthorize("hasAnyRole('STUDENT', 'DEAN','STUDENT_REP')")
@RequiredArgsConstructor
public class SubjectTagController {

    private final SubjectTagService tagService;

    @PostMapping
    @PreAuthorize("hasAuthority('tag:create')")
    @Operation(summary = "DEAN is allowed")
    public ResponseEntity<SubjectTagRO> createTag(@RequestBody @Valid SubjectTagDTO tagDto) throws SubjectTagAlreadyExistsException {
        SubjectTagRO createdTag = tagService.createTag(tagDto);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('tag:read')")
    @Operation(summary = "DEAN,STUDENT,STUDENT_REP are allowed")
    public ResponseEntity<List<SubjectTagRO>> getAllTags(){
        List<SubjectTagRO> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{tagId}")
    @PreAuthorize("hasAuthority('tag:read')")
    @Operation(summary = "DEAN,STUDENT,STUDENT_REP are allowed")
    public ResponseEntity<SubjectTagRO> getTag(@PathVariable Long tagId) throws SubjectTagNotFoundException {
        SubjectTagRO tag = tagService.getTag(tagId);
        return ResponseEntity.ok(tag);
    }

    @PutMapping("/{tagId}")
    @PreAuthorize("hasAuthority('tag:update')")
    @Operation(summary = "DEAN is allowed")
    public ResponseEntity<SubjectTagRO> updateTag(@PathVariable Long tagId, @RequestBody @Valid SubjectTagDTO tagDto) throws SubjectTagNotFoundException {
        SubjectTagRO updatedTag = tagService.updateTag(tagId, tagDto);
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/{tagId}")
    @PreAuthorize("hasAuthority('tag:delete')")
    @Operation(summary = "DEAN is allowed")
    public ResponseEntity<SubjectTagRO> deleteTag(@PathVariable Long tagId) throws SubjectTagNotFoundException {
        SubjectTagRO deletedTag = tagService.deleteTag(tagId);
        return ResponseEntity.ok(deletedTag);
    }
}
