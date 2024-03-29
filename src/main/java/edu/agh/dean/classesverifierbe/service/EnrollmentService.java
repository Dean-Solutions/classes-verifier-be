package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.EnrollDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.repository.EnrollmentRepository;
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

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment assignEnrollmentForUser(EnrollDTO enrollDTO) throws UserNotFoundException, SubjectNotFoundException, EnrollmentAlreadyExistException, SemesterNotFoundException {
        User user = studentService.getRawUserById(enrollDTO.getUserId());
        Subject subject = subjectService.getSubjectById(enrollDTO.getSubjectId());
        Semester currentSemester = semesterService.getCurrentSemester();
        if (enrollmentRepository
                .existsByEnrollStudentAndEnrollSubjectAndSemester(
                        user, subject, currentSemester)) {
            throw new EnrollmentAlreadyExistException();
        }
        return enrollmentRepository.save(convertToEnrollment(user, subject, currentSemester));
    }

    public List<Enrollment> getEnrolledSubjectsByUserId(Long userId) throws UserNotFoundException {
        studentService.getRawUserById(userId);
        return enrollmentRepository.findAllByEnrollStudent_UserId(userId);
    }

    public List<Enrollment> getEnrolledSubjectsByUserIndex(String index) throws UserNotFoundException {
        User user = studentService.findUserByIndexNumber(index);
        return enrollmentRepository.findAllByEnrollStudent_UserId(user.getUserId());
    }

    public Enrollment updateEnrollmentForUser(EnrollDTO enrollDTO) throws UserNotFoundException, SubjectNotFoundException, SemesterNotFoundException, EnrollmentNotFoundException {
        User user = studentService.getRawUserById(enrollDTO.getUserId());
        Subject subject = subjectService.getSubjectById(enrollDTO.getSubjectId());
        Semester currentSemester = semesterService.getCurrentSemester();
        Enrollment currEnrollment = enrollmentRepository.findEnrollmentByEnrollStudentAndEnrollSubjectAndSemester(
                user, subject, currentSemester).orElseThrow(EnrollmentNotFoundException::new);

        currEnrollment.setEnrollStatus(enrollDTO.getEnrollStatus());
        return enrollmentRepository.save(currEnrollment);
    }

    private Enrollment convertToEnrollment(User user, Subject subject, Semester semester) {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollStudent(user);
        enrollment.setEnrollSubject(subject);
        enrollment.setSemester(semester);
        return enrollment;
    }

}
