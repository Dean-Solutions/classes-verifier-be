package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.RO.UserRO;
import edu.agh.dean.classesverifierbe.dto.SubjectDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.service.SubjectService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @PostMapping
    public ResponseEntity<Subject> createSubject(@RequestBody @Valid SubjectDTO subjectDTO) throws SubjectAlreadyExistsException {
        Set<String> tagNames = subjectDTO.getTagNames();
        Subject subject = modelMapper.map(subjectDTO, Subject.class);
        Subject createdSubject = subjectService.createSubject(subject, tagNames);
        return ResponseEntity.ok(createdSubject);
    }
    @PutMapping("/{subjectId}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long subjectId, @RequestBody @Valid SubjectDTO subjectDTO) throws SubjectNotFoundException {
        Subject subject = modelMapper.map(subjectDTO, Subject.class);
        Set<String> tagNames = subjectDTO.getTagNames();
        Subject updatedSubject = subjectService.updateSubject(subjectId, subject, tagNames);
        return ResponseEntity.ok(updatedSubject);
    }

    @GetMapping
    public ResponseEntity<Page<Subject>> getAllSubjects(Pageable pageable,
                                                        @RequestParam(required = false) String tags,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) Integer semester){
        Page<Subject> subjects = subjectService.getAllSubjects(tags, name, semester,pageable);
        return ResponseEntity.ok(subjects);
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


    @GetMapping("/{subjectId}/users")
    public ResponseEntity<List<UserRO>> getUsersEnrolledInSubjectForSemester(@PathVariable Long subjectId,
                                                                              @RequestParam(required = false) Long semesterId) throws SubjectNotFoundException, SemesterNotFoundException {
            List<UserRO> enrolledUsers = subjectService.getUsersEnrolledInSubjectForSemester(subjectId, semesterId);
            return ResponseEntity.ok(enrolledUsers);
    }

    @GetMapping("/{semester}")
    public ResponseEntity<List<Subject>> getSubjectsBySemester(@PathVariable Integer semester) {
        List<Subject> subjects = subjectService.getAllSubjectsBySemester(semester);
        return ResponseEntity.ok(subjects);
    }
}
