package edu.agh.dean.classesverifierbe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class MailService {

    private final JavaMailSender emailSender;

    @Autowired
    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    public void sendRemainder(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Przypomnienie");
        message.setText("Masz nadal nie zatwierdzonych przedmiotów!");
        emailSender.send(message);
    }

    @Async
    public void sendNotification(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Powiadomienie");
        message.setText("Nowy wniosek do rozpatrzenia!\nZaloguj się aby poznać szczegóły");
    }
}
