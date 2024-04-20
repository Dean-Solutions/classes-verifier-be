package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.RO.EnrollmentRO;
import edu.agh.dean.classesverifierbe.dto.EnrollDTO;
import edu.agh.dean.classesverifierbe.dto.SubjectDTO;
import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.dto.MultiEnrollDTO;
import edu.agh.dean.classesverifierbe.dto.EnrollForUserDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.*;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import edu.agh.dean.classesverifierbe.repository.EnrollmentRepository;
import edu.agh.dean.classesverifierbe.specifications.EnrollmentSpecifications;
import lombok.RequiredArgsConstructor;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final SemesterService semesterService;
    private final SubjectService subjectService;
    private final ModelMapper modelMapper;

    public Page<EnrollmentRO> getAllEnrollments(Pageable pageable, String indexNumber, String subjectName, Long semesterId, String statuses, Long userId, Long subjectId) throws SemesterNotFoundException {
        if(semesterId == null){
            semesterId = semesterService.getCurrentSemester().getSemesterId();
        }
        Specification<Enrollment> spec = Specification
                .where(EnrollmentSpecifications.withIndexNumber(indexNumber))
                .and(EnrollmentSpecifications.withSubjectName(subjectName))
                .and(EnrollmentSpecifications.withSemesterId(semesterId))
                .and(EnrollmentSpecifications.withStatuses(statuses))
                .and(EnrollmentSpecifications.withUserId(userId))
                .and(EnrollmentSpecifications.withSubjectId(subjectId));

        Page<Enrollment> enrollments = enrollmentRepository.findAll(spec, pageable);
        List<EnrollmentRO> enrollmentROS = enrollments.getContent().stream()
                .map(this::convertToEnrollmentRO)
                .collect(Collectors.toList());

        return new PageImpl<>(enrollmentROS, pageable, enrollments.getTotalElements());
    }


    private EnrollmentRO convertToEnrollmentRO(Enrollment enrollment) {
        UserDTO userDTO = modelMapper.map(enrollment.getEnrollStudent(), UserDTO.class);
        Subject subject = enrollment.getEnrollSubject();
        Set<SubjectTag> subjectTags = subject.getSubjectTags();
        SubjectDTO subjectDTO = modelMapper.map(enrollment.getEnrollSubject(), SubjectDTO.class);
        subjectDTO.setTagNames(subjectTags.stream().map(SubjectTag::getName).collect(Collectors.toSet()));
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
        checkIfEnrollmentExists(user, subject, currentSemester);
        return enrollmentRepository.save(convertToEnrollment(user, subject, currentSemester, enrollDTO.getEnrollStatus()));
    }

    public List<EnrollmentRO> getEnrolledSubjectsByUserId(EnrollForUserDTO enrollForUserDTO) throws UserNotFoundException, SemesterNotFoundException {
        User user = studentService.getRawUserById(enrollForUserDTO.getUserId());
        Semester semester = getSemesterForEnrollment(enrollForUserDTO.getSemesterId());
        List <Enrollment> enrollments = enrollmentRepository.findAllByEnrollStudentAndSemesterAndEnrollStatusIsIn(user, semester, enrollForUserDTO.getEnrollStatuses());
        return enrollments.stream().map(this::convertToEnrollmentRO).collect(Collectors.toList());
    }

    public List<EnrollmentRO> getEnrolledSubjectsByUserIndex(EnrollForUserDTO enrollForUserDTO) throws UserNotFoundException, SemesterNotFoundException {
        User user = studentService.findUserByIndexNumber(enrollForUserDTO.getIndex());
        Semester semester = getSemesterForEnrollment(enrollForUserDTO.getSemesterId());
        List <Enrollment> enrollments = enrollmentRepository.findAllByEnrollStudentAndSemesterAndEnrollStatusIsIn(user, semester, enrollForUserDTO.getEnrollStatuses());
        return enrollments.stream().map(this::convertToEnrollmentRO).collect(Collectors.toList());
    }

    private Semester getSemesterForEnrollment(Long semesterId) throws SemesterNotFoundException {
        return semesterId == null ? semesterService.getCurrentSemester() : semesterService.getSemesterById(semesterId);
    }

    public EnrollmentRO updateEnrollmentForUser(EnrollDTO enrollDTO) throws UserNotFoundException,
            SubjectNotFoundException,
            SemesterNotFoundException,
            EnrollmentNotFoundException {
        User user = studentService.getRawUserById(enrollDTO.getUserId());
        Subject subject = subjectService.getSubjectById(enrollDTO.getSubjectId());
        Semester currentSemester = semesterService.getCurrentSemester();
        Enrollment currEnrollment = enrollmentRepository.findEnrollmentByEnrollStudentAndEnrollSubjectAndSemester(
                user, subject, currentSemester).orElseThrow(EnrollmentNotFoundException::new);

        currEnrollment.setEnrollStatus(enrollDTO.getEnrollStatus());

        return convertToEnrollmentRO(enrollmentRepository.save(currEnrollment));
    }

    public Optional<Enrollment> getEnrollmentById(Long enrollmentId){
        return enrollmentRepository.findById(enrollmentId);
    }

    public List<Enrollment> getEnrollmentsById(List<Long> enrollmentIds){
        return enrollmentRepository.findAllById(enrollmentIds);
    }

    public EnrollmentRO acceptEnrollment(Long enrollmentId) throws EnrollmentNotFoundException{
        Optional<Enrollment> enrollment = getEnrollmentById(enrollmentId);

        if (enrollment.isPresent()){
            Enrollment enroll = enrollment.get();
            enroll.setEnrollStatus(EnrollStatus.ACCEPTED);
            return convertToEnrollmentRO(enrollmentRepository.save(enroll));
        }
        else {
            throw new EnrollmentNotFoundException();
        }
    }

    public List<EnrollmentRO> acceptEnrollments(List<Long> enrollmentIds) throws EnrollmentNotFoundException{
        List<Enrollment> enrollments = getEnrollmentsById(enrollmentIds);
        if (enrollmentIds.size() != enrollments.size()){
            throw new EnrollmentNotFoundException();
        }
        for (Enrollment enrollment: enrollments){
            enrollment.setEnrollStatus(EnrollStatus.ACCEPTED);
        }
        List<Enrollment> updatedEnrollments = enrollmentRepository.saveAll(enrollments);
        return updatedEnrollments.stream().map(this::convertToEnrollmentRO).collect(Collectors.toList());
    }

    public Enrollment getEnrollmentByUserIdAndSubjectIdAndSemesterId(Long userId, Long subjectId,Long semesterId) throws UserNotFoundException, SubjectNotFoundException,SemesterNotFoundException{
        User user = studentService.getRawUserById(userId);
        Subject subject = subjectService.getSubjectById(subjectId);
        Semester semester = semesterService.getSemesterById(semesterId);
        return enrollmentRepository.findEnrollmentByEnrollStudentAndEnrollSubjectAndSemester(user, subject, semester).orElse(null);
    }

    public List<EnrollmentRO> assignEnrollmentsForMultipleUsers(MultiEnrollDTO multiEnrollDTO) throws UserNotFoundException,
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
        List<Enrollment> savedEnrollments = enrollmentRepository.saveAll(createdEnrollments);
        return savedEnrollments.stream().map(this::convertToEnrollmentRO).collect(Collectors.toList());
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

    public EnrollmentRO deleteEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment != null){
            enrollmentRepository.delete(enrollment);
            return convertToEnrollmentRO(enrollment);
        }
        return null;
    }

    public EnrollmentRO deleteEnrollmentBySubjectUserSemester(EnrollDTO enrollDTO) throws UserNotFoundException, SemesterNotFoundException, SubjectNotFoundException {
        User user = studentService.getRawUserById(enrollDTO.getUserId());
        Subject subject = subjectService.getSubjectById(enrollDTO.getSubjectId());
        Semester semester = getSemesterForEnrollment(enrollDTO.getSemesterId());
        Enrollment enrollment = enrollmentRepository.findEnrollmentByEnrollStudentAndEnrollSubjectAndSemester(user, subject, semester).orElse(null);
        if (enrollment != null){

            enrollmentRepository.delete(enrollment);
            return convertToEnrollmentRO(enrollment);
        }
        return null;
    }

}
