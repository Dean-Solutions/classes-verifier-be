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

import java.util.Optional;

@RestController
@RequestMapping("/request/{requestId}/request-enroll")
public class RequestEnrollController {
    @Autowired
    private RequestEnrollService requestEnrollService;

    @PostMapping()
    public ResponseEntity<RequestEnroll> addRequestEnroll(@PathVariable Long requestId,
                                                          @Valid @RequestBody RequestEnrollDTO requestEnrollDTO)
            throws UserNotFoundException,
            RequestNotFoundException,
            UserInsufficientPermissionException,
            RequestEnrollSingleRequestAlreadyExistsException,
            EnrollmentNotFoundException {
            RequestEnroll newRequestEnroll = requestEnrollService.addRequestEnroll(requestId, requestEnrollDTO);
            return new ResponseEntity<>(newRequestEnroll, HttpStatus.CREATED);
    }

    //TODO taki zwykły get sie przyda raczej
    //TODO delete dodać (np starosta sie walnie przy group add albo ktos zmieni zdanie)

    //
    //
//    @GetMapping("/{id}")
//    public ResponseEntity<RequestEnroll> getRequestEnrollById(@PathVariable Long id, @PathVariable String requestId) {
//        Optional<RequestEnroll> request = requestEnrollService.getRequestEnrollById(id);
//        return request
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
}