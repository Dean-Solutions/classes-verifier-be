package edu.agh.dean.classesverifierbe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Service
public class DeadlineReminderService {

    private final TaskScheduler taskScheduler;
    private final MailService mailService;
    private ScheduledFuture<?> futureTask;

    @Autowired
    public DeadlineReminderService(TaskScheduler taskScheduler, MailService mailService) {
        this.taskScheduler = taskScheduler;
        this.mailService = mailService;
    }

    public void scheduleReminder(Instant reminderTime) {
        // Anulowanie poprzedniego zadania, jeśli istnieje
        if (futureTask != null && !futureTask.isCancelled()) {
            futureTask.cancel(true);
        }

        // Zaplanowanie nowego zadania
        futureTask = taskScheduler.schedule(this::sendRemindingEmail, reminderTime);
    }

    private void sendRemindingEmail() {
        // Logika wysyłania przypomnienia
        mailService.sendRemindingEmail();
        System.out.println("Wysyłanie e-maila przypominającego");
    }

    // Metoda do aktualizacji przypomnienia
    public void updateReminder(Instant newReminderTime) {
        scheduleReminder(newReminderTime);
    }
}
