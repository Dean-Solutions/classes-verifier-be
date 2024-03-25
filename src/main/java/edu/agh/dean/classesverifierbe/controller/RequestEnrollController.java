package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.dto.RequestEnrollDTO;
import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.model.RequestEnroll;
import edu.agh.dean.classesverifierbe.model.enums.RequestType;
import edu.agh.dean.classesverifierbe.service.RequestEnrollService;
import edu.agh.dean.classesverifierbe.service.RequestService;
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
    @Autowired
    private RequestService requestService;
    @PostMapping("/")
    public ResponseEntity<?> addRequestEnroll(@PathVariable Long requestId, @Valid @RequestBody RequestEnrollDTO requestEnrollDTO) {
        try {
            // Retrieve the request entity using the requestId
            // TODO ? Retrive the enrollment entity using the enrollmentId (does it exist) (do i need to check)
            Optional<Request> requestOptional = requestService.getRequestById(requestId);
            if (requestOptional.isPresent()) {
                Request request = requestOptional.get();
                if (request.getRequestType() == RequestType.GROUP) {
                    RequestEnroll newRequestEnroll = requestEnrollService.addRequestEnroll(requestEnrollDTO);
                    return new ResponseEntity<>(newRequestEnroll, HttpStatus.CREATED);
                } else {
                    RequestEnroll newRequestEnroll = requestEnrollService.addRequestEnroll(requestEnrollDTO);
                    return new ResponseEntity<>(newRequestEnroll, HttpStatus.CREATED);
                }
            } else {
                return new ResponseEntity<>("Request with id " + requestId + " not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestEnroll> getRequestEnrollById(@PathVariable Long id) {
        Optional<RequestEnroll> request = requestEnrollService.getRequestEnrollById(id);
        return request
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}