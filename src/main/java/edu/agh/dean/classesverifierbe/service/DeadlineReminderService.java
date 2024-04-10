package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.exceptions.SemesterNotFoundException;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import edu.agh.dean.classesverifierbe.repository.UserRepository;
import edu.agh.dean.classesverifierbe.specifications.UserSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

@Service
public class DeadlineReminderService {

    private final TaskScheduler taskScheduler;
    private final MailService mailService;
    private ScheduledFuture<?> futureTask;
    private final UserRepository userRepository;

    @Autowired
    public DeadlineReminderService(TaskScheduler taskScheduler, MailService mailService, UserRepository userRepository) {
        this.taskScheduler = taskScheduler;
        this.mailService = mailService;
        this.userRepository = userRepository;
        scheduleReminder(Instant.now().plusSeconds(10));
    }

    public void scheduleReminder(Instant reminderTime) {
        // Anulowanie poprzedniego zadania, jeśli istnieje
        if (futureTask != null && !futureTask.isCancelled()) {
            futureTask.cancel(true);
        }

        // Zaplanowanie nowego zadania
        futureTask = taskScheduler.schedule(this::sendRemindingEmail, reminderTime);
    }


    // Metoda do aktualizacji przypomnienia
    public void updateReminder(Instant newReminderTime) {
        scheduleReminder(newReminderTime);
    }


    public void sendRemindingEmail() {

        Set<EnrollStatus> enrollStatuses = new HashSet<>();
        enrollStatuses.add(EnrollStatus.PENDING);
        enrollStatuses.add(EnrollStatus.PROPOSED);
//        try {
//            Semester currentSemester = semesterService.getCurrentSemester();
//            getUsersWithPendingEnrollment(currentSemester, enrollStatuses)
//                    .forEach(user -> mailService.sendRemainder(user.getEmail()));
//        } catch (SemesterNotFoundException ignored) {}

    }

    private List<User> getUsersWithPendingEnrollment(Semester semester, Set<EnrollStatus> enrollStatuses) {
        return userRepository.findAll(
                UserSpecifications.
                        withEnrollmentStatus(enrollStatuses)
                        .and(UserSpecifications.withSemester(semester)));
    }
}
