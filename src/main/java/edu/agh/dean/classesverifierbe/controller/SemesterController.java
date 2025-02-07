package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.dto.SemesterDTO;
import edu.agh.dean.classesverifierbe.exceptions.SemesterAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.SemesterNotFoundException;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.enums.SemesterType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import edu.agh.dean.classesverifierbe.service.SemesterService;

import java.util.List;

@RestController
@RequestMapping("/semesters")
@PreAuthorize("hasAnyRole('DEAN', 'STUDENT_REP', 'STUDENT')")
@RequiredArgsConstructor
public class SemesterController {
    private final SemesterService semesterService;

    @ExceptionHandler(SemesterNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleSemesterNotFound(SemesterNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(SemesterAlreadyExistsException.class)
    @ResponseBody
    public ResponseEntity<String> handleSemesterAlreadyExists(SemesterAlreadyExistsException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
    @PostMapping()
    @ResponseBody
    @PreAuthorize("hasAuthority('semester:create')")
    @Operation(summary = "DEAN is allowed")
    public ResponseEntity<?> createSemester(@Valid @RequestBody SemesterDTO semesterDTO) throws SemesterAlreadyExistsException {
        Semester semester = semesterService.createSemester(semesterDTO);
        return new ResponseEntity<>(semester, HttpStatus.CREATED);

    }

    @GetMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('semesters:read')")
    @Operation(summary = "DEAN,STUDENT_REP are allowed")
    public ResponseEntity<List<Semester>> getAllSemesters() {
        return ResponseEntity.ok(semesterService.getAllSemesters());
    }

    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('semester:read')")
    @Operation(summary = "DEAN,STUDENT_REP,STUDENT are allowed")
    public ResponseEntity<?> getSemester(@PathVariable Long id) throws SemesterNotFoundException {
        Semester semester = semesterService.getSemesterById(id);
        return ResponseEntity.ok(semester);
    }

    @GetMapping("/current")
    @ResponseBody
    @PreAuthorize("hasAuthority('semester:read')")
    @Operation(summary = "DEAN,STUDENT_REP,STUDENT are allowed")
    public ResponseEntity<?> getCurrentSemester() throws SemesterNotFoundException {
        Semester semester = semesterService.getCurrentSemester();
        return ResponseEntity.ok(semester);

    }

    @GetMapping("/year/{year}/type/{type}")
    @ResponseBody
    @PreAuthorize("hasAuthority('semester:read')")
    @Operation(summary = "DEAN,STUDENT_REP,STUDENT are allowed")
    public ResponseEntity<?> getSemesterByYearAndType(@PathVariable Integer year, @PathVariable String type) throws SemesterNotFoundException {
        SemesterType semesterType = SemesterType.valueOf(type);
        Semester semester = semesterService.getSemesterByYearAndType(year, semesterType);
        return ResponseEntity.ok(semester);

    }

    //update current semester
    @PutMapping("/current")
    @ResponseBody
    @PreAuthorize("hasAuthority('semester:update')")
    @Operation(summary = "DEAN,STUDENT_REP are allowed")
    public ResponseEntity<?> updateCurrentSemester(@Valid @RequestBody SemesterDTO semesterDTO) throws SemesterNotFoundException {
        Semester semester = semesterService.updateCurrentSemester(semesterDTO);
        return ResponseEntity.ok(semester);

    }







}
