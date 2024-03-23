package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.dto.SubjectDTO;
import edu.agh.dean.classesverifierbe.RO.SubjectRO; // Make sure to import the RO class
import edu.agh.dean.classesverifierbe.exceptions.SubjectAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.SubjectNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagNotFoundException;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.service.SubjectService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;
    private final ModelMapper modelMapper;

    @Autowired
    public SubjectController(SubjectService subjectService, ModelMapper modelMapper) {
        this.subjectService = subjectService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/")
    public ResponseEntity<Subject> createSubject(@RequestBody @Valid SubjectDTO subjectDTO) throws SubjectAlreadyExistsException {
        Subject subject = modelMapper.map(subjectDTO, Subject.class);
        Subject createdSubject = subjectService.createSubject(subject);
        return ResponseEntity.ok(createdSubject);
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long subjectId,@RequestBody @Valid SubjectDTO subjectDTO) throws SubjectNotFoundException {
        Subject subject = modelMapper.map(subjectDTO, Subject.class);
        Subject updatedSubject = subjectService.updateSubject(subjectId,subject);
        return ResponseEntity.ok(updatedSubject);
    }

    @GetMapping("/")
    public ResponseEntity<List<Subject>> getAllSubjects() throws SubjectNotFoundException{
        List<Subject> subjects = subjectService.getAllSubjects();
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }



    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Subject> deleteSubject(@PathVariable Long subjectId) throws SubjectNotFoundException {
        Subject deletedSubject = subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok(deletedSubject);
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long subjectId) throws SubjectNotFoundException {
        Subject subject = subjectService.getSubjectById(subjectId);
        return ResponseEntity.ok(subject);
    }

    @PostMapping("/{subjectId}/tags/{tagId}")
    public ResponseEntity<Subject> addTagToSubject(@PathVariable Long subjectId, @PathVariable Long tagId) throws SubjectNotFoundException, SubjectTagNotFoundException, SubjectTagAlreadyExistsException {
        Subject updatedSubject = subjectService.addTagToSubject(subjectId, tagId);
        return ResponseEntity.ok(updatedSubject);
    }

    @DeleteMapping("/{subjectId}/tags/{tagId}")
    public ResponseEntity<Subject> removeTagFromSubject(@PathVariable Long subjectId, @PathVariable Long tagId) throws SubjectNotFoundException, SubjectTagNotFoundException{
        Subject updatedSubject = subjectService.removeTagFromSubject(subjectId, tagId);
        return ResponseEntity.ok(updatedSubject);
    }
}
