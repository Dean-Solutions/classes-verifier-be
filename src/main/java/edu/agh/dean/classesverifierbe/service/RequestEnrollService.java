package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.EnrollDTO;
import edu.agh.dean.classesverifierbe.dto.RequestEnrollDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.*;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import edu.agh.dean.classesverifierbe.model.enums.RequestEnrollStatus;
import edu.agh.dean.classesverifierbe.model.enums.RequestType;
import edu.agh.dean.classesverifierbe.repository.EnrollmentRepository;
import edu.agh.dean.classesverifierbe.repository.RequestEnrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.agh.dean.classesverifierbe.model.enums.Role.STUDENT;

@Service
public class RequestEnrollService {
    private final RequestEnrollRepository requestEnrollRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentService enrollmentService;
    private final RequestService requestService;
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final SemesterService semesterService;

    @Autowired
    public RequestEnrollService (RequestEnrollRepository requestEnrollRepository,
                                 EnrollmentRepository enrollmentRepository,
                                 EnrollmentService enrollmentService,
                                 RequestService requestService,
                                 StudentService studentService,
                                 SubjectService subjectService,
                                 SemesterService semesterService){
        this.requestEnrollRepository = requestEnrollRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentService = enrollmentService;
        this.requestService = requestService;
        this.studentService = studentService;
        this.subjectService = subjectService;
        this.semesterService = semesterService;
    }


    public RequestEnroll addRequestEnroll(Long requestId, RequestEnrollDTO requestEnrollDTO)
            throws UserNotFoundException,
            RequestNotFoundException,
            RequestEnrollSingleRequestAlreadyExistsException,
            EnrollmentNotFoundException,
            SubjectNotFoundException,
            SemesterNotFoundException,
            EnrollmentAlreadyExistException {
        RequestEnroll requestEnroll = new RequestEnroll();
        Request request = requestService.getRawRequestById(requestId);
        if (request == null) {
            throw new RequestNotFoundException(requestId);
        }

        //TODO ask if it should be like this? coz dean is no student XD
        User sender = studentService.getRawUserById(requestEnrollDTO.getSenderId());

        if(sender.getRole() == STUDENT &&  !request.getRequestEnrollment().isEmpty())
            throw new RequestEnrollSingleRequestAlreadyExistsException(requestId);

        Subject subject = subjectService.getSubjectById(requestEnrollDTO.getSubjectId());

        //TODO zapytać: raczej szukamy z tego semestru zeby robic requesta? XD
        Semester semester = semesterService.getCurrentSemester();

        User user = studentService.getRawUserById(requestEnrollDTO.getUserId());


        Enrollment enrollment = enrollmentRepository.findEnrollmentByEnrollStudentAndEnrollSubjectAndSemester(user, subject, semester).orElse(null);

        if (enrollment == null) {
            if(request.getRequestType() == RequestType.DELETE){
                throw new EnrollmentNotFoundException();
            }
            else{
                EnrollDTO enrollDTO = EnrollDTO.builder()
                        .userId(user.getUserId())
                        .subjectId(subject.getSubjectId())
                        .enrollStatus(EnrollStatus.PROPOSED)
                        .build();
                enrollment = enrollmentService.assignEnrollmentForUser(enrollDTO);
            }
        }
        else{ //already exists
            if(request.getRequestType() == RequestType.ADD){
                throw new EnrollmentAlreadyExistException();
            }
        }

        requestEnroll.setEnrollment(enrollment);
        requestEnroll.setRequest(request);
        requestEnroll.setRequestStatus(RequestEnrollStatus.PENDING);

        return requestEnrollRepository.save(requestEnroll);
    }

    public Optional<RequestEnroll> getRequestEnrollById(Long requestId, Long id) throws RequestNotFoundException {
        Request request = requestService.getRawRequestById(requestId);
        return requestEnrollRepository.findById(id);
    }

    public List<RequestEnroll> getAllRequestsEnroll(Long requestId) throws RequestNotFoundException {
        Request request = requestService.getRawRequestById(requestId);
        return new ArrayList<>(request.getRequestEnrollment());
    }
}