package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.MailDTO;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MailService {

    private final JavaMailSender emailSender;

    private final EnrollmentRepository enrollmentRepository;


    @Autowired
    public MailService(JavaMailSender emailSender, EnrollmentRepository enrollmentRepository) {
        this.emailSender = emailSender;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Async
    public void sendSimpleMessage(
            MailDTO mailDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getTo());
        message.setSubject(mailDTO.getSubject());
        message.setText(mailDTO.getText());
        emailSender.send(message);
    }

    public void sendRemindingEmail() {

    }

    private List<User> getUsersWithPendingEnrollment() {
        return null;
    }
}
