package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/notification")
    public ResponseEntity<?> sendNotificationEmailToUser() {
        mailService.sendSimpleMessage("wsuski2@gmail.com", "jd", "jdjdjdjd");
        return ResponseEntity.ok().build();
    }
}
