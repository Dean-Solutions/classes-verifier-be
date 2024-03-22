package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.dto.RequestDTO;


import edu.agh.dean.classesverifierbe.dto.RequestEnrollDTO;
import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/request")
public class RequestController {
    @Autowired
    private RequestService requestService;
    @PostMapping("/")
    public ResponseEntity<?> addRequest(@Valid @RequestBody RequestDTO requestDTO) { // why ?
        try {
            Request newRequest = requestService.addRequest(requestDTO);
            return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable Long id) {
        Optional<Request> request = requestService.getRequestById(id);
        return request
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Strategia: tworze request potem dodaje do niego przedmioty które są problematyczne
    //Jak to wgle dziala ze moge dodac czyjes przedmioty do requesta
//    @PostMapping("/{id}")
//    public ResponseEntity<?> addEnrollmentToRequest(@Valid @RequestBody RequestEnrollDTO){
////        try {
////            Request newRequest = requestService.addRequest(requestDTO);
////            return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
////        }
////        catch (Exception e) {
////            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
////        }
//    }


}
