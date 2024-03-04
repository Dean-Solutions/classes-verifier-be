package edu.agh.dean.classesverifierbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


// annotation RestController for project beginning, to be deleted
@SpringBootApplication
@RestController
public class DeanApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeanApplication.class, args);
    }


    // method for project beginning, to be deleted
    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}