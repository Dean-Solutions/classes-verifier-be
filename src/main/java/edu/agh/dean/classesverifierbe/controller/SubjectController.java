package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.RO.UserRO;
import edu.agh.dean.classesverifierbe.dto.SubjectDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.service.AuthContextService;
import edu.agh.dean.classesverifierbe.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/subjects")
@PreAuthorize("hasAnyRole('STUDENT', 'DEAN','STUDENT_REP')")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;
    private final ModelMapper modelMapper;
    private final AuthContextService authContextService;

    @PostMapping
    @PreAuthorize("hasAuthority('subject:create')")
    public ResponseEntity<Subject> createSubject(@RequestBody @Valid SubjectDTO subjectDTO) throws SubjectAlreadyExistsException {
        Set<String> tagNames = subjectDTO.getTagNames();
        Subject subject = modelMapper.map(subjectDTO, Subject.class);
        Subject createdSubject = subjectService.createSubject(subject, tagNames);
        return ResponseEntity.ok(createdSubject);
    }
    @PutMapping("/{subjectId}")
    @PreAuthorize("hasAuthority('subject:update')")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long subjectId, @RequestBody @Valid SubjectDTO subjectDTO) throws SubjectNotFoundException {
        Subject subject = modelMapper.map(subjectDTO, Subject.class);
        Set<String> tagNames = subjectDTO.getTagNames();
        Subject updatedSubject = subjectService.updateSubject(subjectId, subject, tagNames);
        return ResponseEntity.ok(updatedSubject);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('subject:read')")
    public ResponseEntity<Page<Subject>> getAllSubjects(Pageable pageable,
                                                        @RequestParam(required = false) String tags,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) Integer semester){

        Role role =authContextService.getCurrentRole();
        System.out.println(role);
        User user = authContextService.getCurrentUser();
        System.out.println(user);

        Page<Subject> subjects = subjectService.getAllSubjects(tags, name, semester,pageable);
        return ResponseEntity.ok(subjects);
    }

    @DeleteMapping("/{subjectId}")
    @PreAuthorize("hasAuthority('subject:delete')")
    public ResponseEntity<Subject> deleteSubject(@PathVariable Long subjectId) throws SubjectNotFoundException {
        Subject deletedSubject = subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok(deletedSubject);
    }

    @GetMapping("/{subjectId}")
    @PreAuthorize("hasAuthority('subject:read')")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long subjectId) throws SubjectNotFoundException {
        Subject subject = subjectService.getSubjectById(subjectId);
        return ResponseEntity.ok(subject);
    }


    @GetMapping("/{subjectId}/users")
    @PreAuthorize("hasAuthority('subject:read')")
    public ResponseEntity<List<UserRO>> getUsersEnrolledInSubjectForSemester(@PathVariable Long subjectId,
                                                                              @RequestParam(required = false) Long semesterId) throws SubjectNotFoundException, SemesterNotFoundException {
            List<UserRO> enrolledUsers = subjectService.getUsersEnrolledInSubjectForSemester(subjectId, semesterId);
            return ResponseEntity.ok(enrolledUsers);
    }

    @GetMapping("/semester/{semester}")
    @PreAuthorize("hasAuthority('subject:read')")
    public ResponseEntity<List<Subject>> getSubjectsBySemester(@PathVariable Integer semester) {
        List<Subject> subjects = subjectService.getAllSubjectsBySemester(semester);
        return ResponseEntity.ok(subjects);
    }
}
