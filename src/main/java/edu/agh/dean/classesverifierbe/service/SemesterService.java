package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.SemesterDTO;
import edu.agh.dean.classesverifierbe.exceptions.SemesterAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.SemesterNotFoundException;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import edu.agh.dean.classesverifierbe.repository.SemesterRepository;
import edu.agh.dean.classesverifierbe.service.mail.MailHelperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import edu.agh.dean.classesverifierbe.model.enums.SemesterType;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
public class SemesterService {

    private final SemesterRepository semesterRepository;
    private final StudentService studentService;
    private final TaskScheduler taskScheduler;
    private final MailHelperService mailHelperService;
    private ScheduledFuture<?> futureTask;

    @Autowired
    public SemesterService(SemesterRepository semesterRepository, StudentService studentService, TaskScheduler taskScheduler, MailHelperService mailHelperService) {
        this.semesterRepository = semesterRepository;
        this.studentService = studentService;
        this.taskScheduler = taskScheduler;
        this.mailHelperService = mailHelperService;
    }

    public Semester getSemesterById(Long id) throws SemesterNotFoundException  {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new SemesterNotFoundException("Semester not found with id: " + id));
    }

    public Semester getSemesterByYearAndType(Integer year, SemesterType semesterType) throws SemesterNotFoundException{
        return semesterRepository.findByYearAndSemesterType(year, semesterType)
                .orElseThrow(() -> new SemesterNotFoundException("Semester not found with year: " + year + " and type: " + semesterType));
    }

    public Semester getCurrentSemester() throws SemesterNotFoundException{
        return semesterRepository.findCurrentSemester()
                .orElseThrow(() -> new SemesterNotFoundException("Current semester not found"));
    }

    private boolean isSemesterStarted() {
        try {
            getCurrentSemester();
            return true;
        } catch (SemesterNotFoundException ignored) {
            return false;
        }
    }


    public Semester createSemester(SemesterDTO semesterDTO) throws SemesterAlreadyExistsException{
        if (isSemesterStarted()) {
            throw new SemesterAlreadyExistsException("Semester already started");
        }
        Semester semester = new Semester();
        semester.setSemesterType(semesterDTO.getSemesterType());
        semester.setYear(semesterDTO.getYear());
        Semester foundSemester = semesterRepository.findByYearAndSemesterType(semesterDTO.getYear(), semesterDTO.getSemesterType()).orElse(null);
        if (foundSemester != null) {
            throw new SemesterAlreadyExistsException("Semester already exists with year: " + semesterDTO.getYear() + " and type: " + semesterDTO.getSemesterType());
        }
        semester.setDeadline(semesterDTO.getDeadline());
        scheduleReminder(semesterDTO.getReminderBeforeDeadline().atZone(ZoneId.systemDefault()).toInstant());
        return semesterRepository.save(semester);
    }

    public Semester updateCurrentSemester(SemesterDTO semesterDTO) throws SemesterNotFoundException{
        Semester semester = semesterRepository.findCurrentSemester().orElse(null);
        if (semester == null) {
            throw new SemesterNotFoundException("Current semester not found");
        }
        semester.setSemesterType(semesterDTO.getSemesterType());
        semester.setYear(semesterDTO.getYear());
        semester.setDeadline(semesterDTO.getDeadline());
        scheduleReminder(semesterDTO.getReminderBeforeDeadline().atZone(ZoneId.systemDefault()).toInstant());
        return semesterRepository.save(semester);
    }

    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }


    public void scheduleReminder(Instant reminderTime) {
        if (futureTask != null && !futureTask.isCancelled()) {
            futureTask.cancel(true);
        }

        futureTask = taskScheduler.schedule(this::sendRemindingEmail, reminderTime);
        log.info("task scheduled " + reminderTime);
    }

    public void sendRemindingEmail() {
        Set<EnrollStatus> enrollStatuses = new HashSet<>();
        enrollStatuses.add(EnrollStatus.PENDING);
        enrollStatuses.add(EnrollStatus.PROPOSED);
        try {
            Semester currentSemester = getCurrentSemester();
            studentService.getUsersWithPendingEnrollment(currentSemester, enrollStatuses)
                    .forEach(mailHelperService::sendNotification);

        } catch (SemesterNotFoundException exception) {
            log.error("Cannot send notification because there is not current semester" + exception);
        }
    }

}
