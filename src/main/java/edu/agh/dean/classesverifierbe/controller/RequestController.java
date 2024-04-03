package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.RO.RequestRO;
import edu.agh.dean.classesverifierbe.dto.RequestDTO;


import edu.agh.dean.classesverifierbe.exceptions.RequestNotFoundException;
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
    public ResponseEntity<Request> addRequest(@Valid @RequestBody RequestDTO requestDTO) throws UserNotFoundException {
            Request newRequest = requestService.addRequest(requestDTO);
            return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<RequestRO> getRequestById(@PathVariable Long id) throws RequestNotFoundException {
        RequestRO requestRO = requestService.getRequestById(id);
        return ResponseEntity.ok(requestRO);
    }

    //TODO get all requests with status
    //TODO wszystkie requesty usera
    //TODO ogólny get

    @DeleteMapping("/{id}")
    public ResponseEntity<RequestRO> deleteRequestById(@PathVariable Long id) throws RequestNotFoundException {
        RequestRO requestRO = requestService.deleteRequestById(id);
        return ResponseEntity.ok(requestRO);
    }
}
