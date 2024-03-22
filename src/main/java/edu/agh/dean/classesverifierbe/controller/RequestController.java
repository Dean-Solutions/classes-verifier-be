package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class RequestController {
    @Autowired
    private RequestService requestService;

}
