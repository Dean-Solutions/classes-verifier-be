package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.dto.SemesterDTO;
import edu.agh.dean.classesverifierbe.exceptions.SemesterAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.SemesterNotFoundException;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.enums.SemesterType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import edu.agh.dean.classesverifierbe.service.SemesterService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/semesters")
public class SemesterController {

    @Autowired
    private  SemesterService semesterService;

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
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> createSemester(@Valid @RequestBody SemesterDTO semesterDTO) {
        try {
            Semester semester = semesterService.createSemester(semesterDTO);
            return new ResponseEntity<>(semester, HttpStatus.CREATED);
        } catch (SemesterAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> getAllSemesters() {
        return ResponseEntity.ok(semesterService.getAllSemesters());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getSemester(@PathVariable Long id) {
        try {
            Semester semester = semesterService.getSemesterById(id);
            return ResponseEntity.ok(semester);
        } catch (SemesterNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/current")
    @ResponseBody
    public ResponseEntity<?> getCurrentSemester() {
        try {
            Semester semester = semesterService.getCurrentSemester();
            return ResponseEntity.ok(semester);
        } catch (SemesterNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/year/{year}/type/{type}")
    @ResponseBody
    public ResponseEntity<?> getSemesterByYearAndType(@PathVariable Integer year, @PathVariable String type) {
        try {
            SemesterType semesterType = SemesterType.valueOf(type);
            Semester semester = semesterService.getSemesterByYearAndType(year, semesterType);
            return ResponseEntity.ok(semester);
        } catch (SemesterNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //update current semester
    @PutMapping("/current")
    @ResponseBody
    public ResponseEntity<?> updateCurrentSemester(@Valid @RequestBody SemesterDTO semesterDTO) {
        try {
            Semester semester = semesterService.updateCurrentSemester(semesterDTO);
            return ResponseEntity.ok(semester);
        } catch (SemesterNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }







}
