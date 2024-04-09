package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.RO.EnrollmentRO;
import edu.agh.dean.classesverifierbe.dto.EnrollDTO;
import edu.agh.dean.classesverifierbe.dto.SubjectDTO;
import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import edu.agh.dean.classesverifierbe.repository.EnrollmentRepository;
import edu.agh.dean.classesverifierbe.specifications.EnrollmentSpecifications;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final SemesterService semesterService;
    private final SubjectService subjectService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentService studentService, SemesterService semesterService, SubjectService subjectService) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentService = studentService;
        this.semesterService = semesterService;
        this.subjectService = subjectService;
    }

//    public List<Enrollment> getAllEnrollments() {
//        return enrollmentRepository.findAll();
//    }

    public Page<EnrollmentRO> getAllEnrollments(Pageable pageable, String indexNumber, String subjectName, Long semesterId, String status)
            throws SemesterNotFoundException {
        if(semesterId == null){
            semesterId = semesterService.getCurrentSemester().getSemesterId();
        }
        Specification<Enrollment> spec = Specification
                .where(EnrollmentSpecifications.withIndexNumber(indexNumber))
                .and(EnrollmentSpecifications.withSubjectName(subjectName))
                .and(EnrollmentSpecifications.withSemesterId(semesterId))
                .and(EnrollmentSpecifications.withStatus(status));
        Page<Enrollment> enrollments = enrollmentRepository.findAll(spec, pageable);
        List<EnrollmentRO> enrollmentROS = enrollments.getContent().stream()
                .map(this::convertToEnrollmentRO)
                .collect(Collectors.toList());

        return new PageImpl<>(enrollmentROS, pageable, enrollments.getTotalElements());
    }

    private EnrollmentRO convertToEnrollmentRO(Enrollment enrollment) {
        UserDTO userDTO = modelMapper.map(enrollment.getEnrollStudent(), UserDTO.class);
        SubjectDTO subjectDTO = modelMapper.map(enrollment.getEnrollSubject(), SubjectDTO.class);

        return EnrollmentRO.builder()
                .enrollmentId(enrollment.getEnrollmentId())
                .enrollStatus(enrollment.getEnrollStatus())
                .user(userDTO)
                .subject(subjectDTO)
                .semester(enrollment.getSemester())
                .build();
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
