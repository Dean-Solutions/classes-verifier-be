package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.EnrollDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import edu.agh.dean.classesverifierbe.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if (enrollmentRepository
                .existsByEnrollStudentAndEnrollSubjectAndSemester(
                        user, subject, currentSemester)) {
            throw new EnrollmentAlreadyExistException();
        }
        return enrollmentRepository.save(convertToEnrollment(user, subject, currentSemester, enrollDTO.getEnrollStatus()));
    }

    public List<Enrollment> getEnrolledSubjectsByUserId(Long userId, Long semesterId) throws UserNotFoundException, SemesterNotFoundException {
        User user = studentService.getRawUserById(userId);
        Semester semester = getSemesterForEnrollment(semesterId);
        return enrollmentRepository.findAllByEnrollStudentAndSemester(user, semester);
    }

    public List<Enrollment> getEnrolledSubjectsByUserIndex(String index, Long semesterId) throws UserNotFoundException, SemesterNotFoundException {
        User user = studentService.findUserByIndexNumber(index);
        Semester semester = getSemesterForEnrollment(semesterId);
        return enrollmentRepository.findAllByEnrollStudentAndSemester(user, semester);
    }

    private Semester getSemesterForEnrollment(Long semesterId) throws SemesterNotFoundException {
        return semesterId == null ? semesterService.getCurrentSemester() : semesterService.getSemesterById(semesterId);
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

}
