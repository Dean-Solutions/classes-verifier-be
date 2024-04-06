package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.dto.RequestEnrollDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.RequestEnroll;
import edu.agh.dean.classesverifierbe.service.RequestEnrollService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/request/{requestId}/request-enroll")
public class RequestEnrollController {
    private final RequestEnrollService requestEnrollService;

    @Autowired
    public RequestEnrollController(RequestEnrollService requestEnrollService){
        this.requestEnrollService = requestEnrollService;
    }

    @PostMapping
    public ResponseEntity<RequestEnroll> addRequestEnroll(@PathVariable Long requestId,
                                                          @Valid @RequestBody RequestEnrollDTO requestEnrollDTO)
            throws UserNotFoundException,
            RequestNotFoundException,
            RequestEnrollSingleRequestAlreadyExistsException,
            EnrollmentNotFoundException, SubjectNotFoundException,
            SemesterNotFoundException,
            EnrollmentAlreadyExistException {
            RequestEnroll newRequestEnroll = requestEnrollService.addRequestEnroll(requestId, requestEnrollDTO);
            return new ResponseEntity<>(newRequestEnroll, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<RequestEnroll>> getRequestEnroll(@PathVariable Long requestId) throws RequestNotFoundException {
        List<RequestEnroll> newRequestEnroll = requestEnrollService.getAllRequestsEnroll(requestId);
        return new ResponseEntity<>(newRequestEnroll, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    //napisac

    //TODO w sumie to nie wiem czy chcemy taki endpoint wgle v (i czy to git zapisane)
    @GetMapping("/{id}")
    public ResponseEntity<RequestEnroll> getRequestEnrollById(@PathVariable Long requestId, @PathVariable Long id) throws RequestNotFoundException {
        Optional<RequestEnroll> request = requestEnrollService.getRequestEnrollById(requestId, id);
        return request
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}