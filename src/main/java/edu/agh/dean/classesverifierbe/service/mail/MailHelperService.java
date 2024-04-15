package edu.agh.dean.classesverifierbe.service.mail;

import edu.agh.dean.classesverifierbe.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MailHelperService {

    private static final String NOTIFICATION_TEMPLATE = "notificationEmailTemplate.ftl";
    private static final String NOTIFICATION_DEAN_TEMPLATE = "notificationDeanEmailTemplate.ftl";
    private static final String NOTIFICATION_SUBJECT = "Powiadomienie";

    private final MailService mailService;

    @Autowired
    public MailHelperService(MailService mailService) {
        this.mailService = mailService;
    }

    public void sendNotification(User user) {
        mailService.sendHtmlEmail(user.getEmail(), NOTIFICATION_SUBJECT, NOTIFICATION_TEMPLATE, prepareNotificationData(user));
    }

    public void sendDeanNotification(User user) {
        mailService.sendHtmlEmail(user.getEmail(), NOTIFICATION_SUBJECT, NOTIFICATION_DEAN_TEMPLATE, prepareNotificationData(user));
    }

    private Map<String, Object> prepareNotificationData(User user) {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("firstname", user.getFirstName());
        templateData.put("lastname", user.getLastName());
        return templateData;
    }
}
