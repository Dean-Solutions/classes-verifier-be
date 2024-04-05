package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.dto.MailDTO;
import edu.agh.dean.classesverifierbe.service.MailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public ResponseEntity<?> sendNotificationEmailToUser(@RequestBody @Valid MailDTO mailDTO) {
        mailService.sendSimpleMessage(mailDTO);
        return ResponseEntity.ok().build();
    }
}
