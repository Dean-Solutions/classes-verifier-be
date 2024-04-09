package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.EnrollDTO;
import edu.agh.dean.classesverifierbe.dto.MultiEnrollDTO;
import edu.agh.dean.classesverifierbe.dto.EnrollForUserDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import edu.agh.dean.classesverifierbe.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        Semester currentSemester = getSemesterForEnrollment(enrollDTO.getSemesterId());
        checkIfEnrollmentExists(user, subject, currentSemester);
        return enrollmentRepository.save(convertToEnrollment(user, subject, currentSemester, enrollDTO.getEnrollStatus()));
    }

    public List<Enrollment> getEnrolledSubjectsByUserId(EnrollForUserDTO enrollForUserDTO) throws UserNotFoundException, SemesterNotFoundException {
        User user = studentService.getRawUserById(enrollForUserDTO.getUserId());
        Semester semester = getSemesterForEnrollment(enrollForUserDTO.getSemesterId());
        return enrollmentRepository.findAllByEnrollStudentAndSemesterAndEnrollStatusIsIn(user, semester, enrollForUserDTO.getEnrollStatuses());
    }

    public List<Enrollment> getEnrolledSubjectsByUserIndex(EnrollForUserDTO enrollForUserDTO) throws UserNotFoundException, SemesterNotFoundException {
        User user = studentService.findUserByIndexNumber(enrollForUserDTO.getIndex());
        Semester semester = getSemesterForEnrollment(enrollForUserDTO.getSemesterId());
        return enrollmentRepository.findAllByEnrollStudentAndSemesterAndEnrollStatusIsIn(user, semester, enrollForUserDTO.getEnrollStatuses());
    }

    private Semester getSemesterForEnrollment(Long semesterId) throws SemesterNotFoundException {
        return semesterId == null ? semesterService.getCurrentSemester() : semesterService.getSemesterById(semesterId);
    }

    public Enrollment updateEnrollmentForUser(EnrollDTO enrollDTO) throws UserNotFoundException,
            SubjectNotFoundException,
            SemesterNotFoundException,
            EnrollmentNotFoundException {
        User user = studentService.getRawUserById(enrollDTO.getUserId());
        Subject subject = subjectService.getSubjectById(enrollDTO.getSubjectId());
        Semester currentSemester = semesterService.getCurrentSemester();
        Enrollment currEnrollment = enrollmentRepository.findEnrollmentByEnrollStudentAndEnrollSubjectAndSemester(
                user, subject, currentSemester).orElseThrow(EnrollmentNotFoundException::new);

        currEnrollment.setEnrollStatus(enrollDTO.getEnrollStatus());
        return enrollmentRepository.save(currEnrollment);
    }

    public Optional<Enrollment> getEnrollmentById(Long enrollmentId){
        return enrollmentRepository.findById(enrollmentId);
    }

    public List<Enrollment> getEnrollmentsById(List<Long> enrollmentIds){
        return enrollmentRepository.findAllById(enrollmentIds);
    }

    public Enrollment acceptEnrollment(Long enrollmentId) throws EnrollmentNotFoundException{
        Optional<Enrollment> enrollment = getEnrollmentById(enrollmentId);

        if (enrollment.isPresent()){
            Enrollment enroll = enrollment.get();
            enroll.setEnrollStatus(EnrollStatus.ACCEPTED);
            return enrollmentRepository.save(enroll);
        }
        else {
            throw new EnrollmentNotFoundException();
        }
    }

    public List<Enrollment> acceptEnrollments(List<Long> enrollmentIds) throws EnrollmentNotFoundException{
        List<Enrollment> enrollments = getEnrollmentsById(enrollmentIds);
        if (enrollmentIds.size() != enrollments.size()){
            throw new EnrollmentNotFoundException();
        }
        for (Enrollment enrollment: enrollments){
            enrollment.setEnrollStatus(EnrollStatus.ACCEPTED);
        }
        enrollmentRepository.saveAll(enrollments);
        return enrollments;
    }

    public Enrollment getEnrollmentByUserIdAndSubjectIdAndSemesterId(Long userId, Long subjectId,Long semesterId) throws UserNotFoundException, SubjectNotFoundException,SemesterNotFoundException{
        User user = studentService.getRawUserById(userId);
        Subject subject = subjectService.getSubjectById(subjectId);
        Semester semester = semesterService.getSemesterById(semesterId);
        return enrollmentRepository.findEnrollmentByEnrollStudentAndEnrollSubjectAndSemester(user, subject, semester).orElse(null);
    }

    public List<Enrollment> assignEnrollmentsForMultipleUsers(MultiEnrollDTO multiEnrollDTO) throws UserNotFoundException,
            SubjectNotFoundException,
            SemesterNotFoundException,
            EnrollmentAlreadyExistException {

        List<Enrollment> createdEnrollments = new ArrayList<>();
        Semester currentSemester = getSemesterForEnrollment(multiEnrollDTO.getSemesterId());
        for (Map.Entry<Long, Set<Long>> entry : multiEnrollDTO.getSubjectsToStudents().entrySet()) {
            Subject subject = subjectService.getSubjectById(entry.getKey());
            for (Long userId : entry.getValue()) {
                User user = studentService.getRawUserById(userId);
                checkIfEnrollmentExists(user, subject, currentSemester);
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollStudent(user);
                enrollment.setEnrollSubject(subject);
                enrollment.setSemester(currentSemester);
                enrollment.setEnrollStatus(multiEnrollDTO.getEnrollStatus());
                createdEnrollments.add(enrollment);
            }
        }
        return enrollmentRepository.saveAll(createdEnrollments);
    }

    private void checkIfEnrollmentExists(User user, Subject subject, Semester currentSemester) throws EnrollmentAlreadyExistException {
        if (enrollmentRepository
                .existsByEnrollStudentAndEnrollSubjectAndSemester(
                        user, subject, currentSemester)) {
            throw new EnrollmentAlreadyExistException();
        }
    }

    private Enrollment convertToEnrollment(User user, Subject subject, Semester semester, EnrollStatus enrollStatus) {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollStudent(user);
        enrollment.setEnrollSubject(subject);
        enrollment.setSemester(semester);
        if(enrollStatus != null){
            enrollment.setEnrollStatus(enrollStatus);
        }
        return enrollment;
    }
}
