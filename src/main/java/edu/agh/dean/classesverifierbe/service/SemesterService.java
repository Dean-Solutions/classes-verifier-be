package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.SemesterDTO;
import edu.agh.dean.classesverifierbe.exceptions.SemesterAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.SemesterNotFoundException;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.agh.dean.classesverifierbe.model.enums.SemesterType;

import java.time.Instant;
import java.util.List;

@Service
public class SemesterService {
    @Autowired
    private SemesterRepository semesterRepository;
    private DeadlineReminderService deadlineReminderService;

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


    public Semester createSemester(SemesterDTO semesterDTO) throws SemesterAlreadyExistsException{
        Semester semester = new Semester();
        semester.setSemesterType(semesterDTO.getSemesterType());
        semester.setYear(semesterDTO.getYear());
        Semester foundSemester = semesterRepository.findByYearAndSemesterType(semesterDTO.getYear(), semesterDTO.getSemesterType()).orElse(null);
        if (foundSemester != null) {
            throw new SemesterAlreadyExistsException("Semester already exists with year: " + semesterDTO.getYear() + " and type: " + semesterDTO.getSemesterType());
        }
        semester.setDeadline(semesterDTO.getDeadline());
        deadlineReminderService.scheduleReminder(Instant.from(semesterDTO.getDeadline()));
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
        return semesterRepository.save(semester);
    }

    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }
}
