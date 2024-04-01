package edu.agh.dean.classesverifierbe.service;


import edu.agh.dean.classesverifierbe.exceptions.SemesterNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.repository.EnrollmentRepository;

import edu.agh.dean.classesverifierbe.dto.EnrollDTO;
import edu.agh.dean.classesverifierbe.exceptions.EnrollmentAlreadyExistException;
import edu.agh.dean.classesverifierbe.exceptions.SemesterNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.SubjectNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.repository.EnrollmentRepository;
import edu.agh.dean.classesverifierbe.repository.SubjectRepository;

import edu.agh.dean.classesverifierbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {


    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final SemesterService semesterService;
    private final SubjectService subjectService;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentService studentService, SemesterService semesterService, SubjectService subjectService) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentService = studentService;
        this.semesterService = semesterService;
        this.subjectService = subjectService;
    }

    public Enrollment assignEnrollmentForUser(EnrollDTO enrollDTO) throws UserNotFoundException, SubjectNotFoundException, EnrollmentAlreadyExistException, SemesterNotFoundException {
        User user = studentService.getRawUserById(enrollDTO.getUserId());
        Subject subject = subjectService.getSubjectById(enrollDTO.getSubjectId());
        Semester currentSemester = semesterService.getCurrentSemester();
        if (enrollmentRepository
                .existsByEnrollStudent_UserIdAndEnrollSubject_SubjectIdAndSemester(
                        enrollDTO.getUserId(), enrollDTO.getSubjectId(), currentSemester)) {
            throw new EnrollmentAlreadyExistException();
        }
        return enrollmentRepository.save(convertToEnrollment(user, subject, currentSemester));
    }

    public Enrollment convertToEnrollment(User user, Subject subject, Semester semester) {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollStudent(user);
        enrollment.setEnrollSubject(subject);
        enrollment.setSemester(semester);
        return enrollment;
    }

    public List<Enrollment> getAllEnrollmentForUser(EnrollDTO enrollDTO) throws UserNotFoundException {
        studentService.getRawUserById(enrollDTO.getUserId());
        return enrollmentRepository.findAllByEnrollStudent_UserId(enrollDTO.getUserId());
    }

}
