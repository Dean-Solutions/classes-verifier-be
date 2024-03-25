package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.dto.RequestDTO;


import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;


@RestController
@RequestMapping("/request")
public class RequestController {
    @Autowired
    private RequestService requestService;
    @PostMapping
    public ResponseEntity<?> addRequest(@Valid @RequestBody RequestDTO requestDTO) throws UserNotFoundException {
            Request newRequest = requestService.addRequest(requestDTO);
            return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable Long id) {
        Optional<Request> request = requestService.getRequestById(id);
        return request
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

// TODO possibly we want to delete requests
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteRequestById(@PathVariable Long id) {
//        try{
//            requestService.deleteRequestById(id);
//        }
//        catch  {
//
//        }
//    }
}
