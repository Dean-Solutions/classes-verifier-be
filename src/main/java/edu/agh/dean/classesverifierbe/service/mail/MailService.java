package edu.agh.dean.classesverifierbe.service.mail;

import freemarker.template.*;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Map;


@Component
@Slf4j
public class MailService {

    private final JavaMailSender emailSender;

    private final Configuration freemarkerConfig;


    @Autowired
    public MailService(JavaMailSender emailSender, Configuration freemarkerConfig) {
        this.emailSender = emailSender;
        this.freemarkerConfig = freemarkerConfig;
    }

    @Async
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> templateData) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper mailMessage = new MimeMessageHelper(message, true, "UTF-8");
            Template template = freemarkerConfig.getTemplate(templateName);
            String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateData);
            mailMessage.setText(emailContent, true);
            mailMessage.setSubject(subject);
            mailMessage.setTo(to);
            emailSender.send(message);
            log.info("Email send");

        } catch (Exception exception) {
            log.error("Error while sending mail " + exception);
        }
    }


}
