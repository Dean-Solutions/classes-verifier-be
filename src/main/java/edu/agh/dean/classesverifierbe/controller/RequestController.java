package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
public class RequestController {
    @Autowired
    private RequestService requestService;
    @GetMapping("/")
    public ResponseEntity<Page<Request>> getStudents(Pageable pageable,
                                                     @RequestParam(required = false) String description,
                                                     @RequestParam(required = false) String status,
                                                     @RequestParam(required = false) String lastName,
                                                     @RequestParam(required = false) String submissionDate,
                                                     @RequestParam(required = false) Integer requestType) {
//        Page<User> users = requestService.ge(pageable, tag, name, lastName, indexNumber, semester,status);
//        if(users.isEmpty()){
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(users);
        return null;
    }

}
