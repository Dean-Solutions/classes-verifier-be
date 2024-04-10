package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.RO.RequestEnrollRO;
import edu.agh.dean.classesverifierbe.RO.RequestRO;
import edu.agh.dean.classesverifierbe.dto.*;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.*;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import edu.agh.dean.classesverifierbe.repository.RequestEnrollRepository;
import edu.agh.dean.classesverifierbe.repository.RequestRepository;

import edu.agh.dean.classesverifierbe.repository.UserRepository;
import edu.agh.dean.classesverifierbe.specifications.RequestSpecifications;
import edu.agh.dean.classesverifierbe.specifications.UserSpecifications;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.agh.dean.classesverifierbe.model.enums.RequestEnrollStatus.ACCEPTED;
import static edu.agh.dean.classesverifierbe.model.enums.RequestEnrollStatus.PENDING;

@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RequestEnrollRepository requestEnrollRepository;
    private final EnrollmentService enrollmentService;
    private final SemesterService semesterService;
    private final MailService mailService;
    @Autowired
    public RequestService(RequestRepository requestRepository, UserRepository userRepository,
                          ModelMapper modelMapper, RequestEnrollRepository requestEnrollRepository,
                          EnrollmentService enrollmentService, SemesterService semesterService,
                          MailService mailService) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.requestEnrollRepository = requestEnrollRepository;
        this.enrollmentService = enrollmentService;
        this.semesterService = semesterService;
        this.mailService = mailService;
    }

    public RequestRO getRequestById(Long id) throws RequestNotFoundException{
        Request request = getRawRequestById(id);
        return convertToRequestRO(request);
    }

    public Request getRawRequestById(Long id) throws RequestNotFoundException {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException("id", id.toString()));
    }

    public Page<RequestRO> getRequestByCriteria(Pageable pageable, String requestType, String senderId) {
        Specification<Request> spec = Specification
                .where(RequestSpecifications.withRequestType(requestType))
                .and(RequestSpecifications.withSenderId(senderId));

        Page<Request> requests = requestRepository.findAll(spec, pageable);
        List<RequestRO> requestROs = requests.getContent().stream()
                .map(this::convertToRequestRO)
                .toList();

        return new PageImpl<>(requestROs, pageable, requests.getTotalElements());
    }



    public RequestRO createRequest(RequestDTO requestDTO) throws UserNotFoundException, SemesterNotFoundException, SubjectNotFoundException, EnrollmentAlreadyExistException {
        User sender = userRepository.findById(requestDTO.getSenderId())
                .orElseThrow(() -> new UserNotFoundException(requestDTO.getSenderId().toString()));

        Request request = Request.builder()
                .description(requestDTO.getDescription())
                .submissionDate(requestDTO.getSubmissionDate())
                .requestType(requestDTO.getRequestType())
                .user(sender)
                .build();
        request.setRequestEnrollment(new HashSet<>());
        for (RequestEnrollDTO reDTO : requestDTO.getRequestEnrolls()) {
            Semester semester = reDTO.getSemesterId() != null ? semesterService.getSemesterById(reDTO.getSemesterId()) : semesterService.getCurrentSemester();
            Enrollment enrollment = enrollmentService.getEnrollmentByUserIdAndSubjectIdAndSemesterId(reDTO.getUserId(), reDTO.getSubjectId(), semester.getSemesterId());
             if(enrollment == null){
                    enrollment = enrollmentService.assignEnrollmentForUser(EnrollDTO.builder()
                            .userId(reDTO.getUserId())
                            .subjectId(reDTO.getSubjectId())
                            .enrollStatus(EnrollStatus.PROPOSED)
                            .build());
            }
            RequestEnroll requestEnroll = RequestEnroll.builder()
                    .request(request)
                    .enrollment(enrollment)
                    .requestStatus(PENDING)
                    .build();
            request.getRequestEnrollment().add(requestEnroll);
        }
        Request savedRequest = requestRepository.save(request);
        notifyDeans();
        return convertToRequestRO(savedRequest);
    }
    @Transactional
    public RequestRO updateRequest(RequestDTO requestDTO) throws RequestEnrollNotFoundException, UserNotFoundException, SubjectNotFoundException, SemesterNotFoundException, EnrollmentNotFoundException {
        Request request = requestRepository.findById(requestDTO.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        request.setDescription(requestDTO.getDescription());
        request.setSubmissionDate(requestDTO.getSubmissionDate());
        request.setRequestType(requestDTO.getRequestType());

        for (RequestEnrollDTO reDTO : requestDTO.getRequestEnrolls()) {
            RequestEnroll requestEnroll = requestEnrollRepository.findById(reDTO.getRequestEnrollId())
                    .orElseThrow(() -> new RequestEnrollNotFoundException("id", reDTO.getRequestEnrollId().toString()));
            requestEnroll.setRequestStatus(reDTO.getRequestStatus());
            if (requestEnroll.getRequestStatus() == ACCEPTED) {
                processEnrollmentChange(requestDTO, reDTO);
            }
            requestEnrollRepository.save(requestEnroll);
        }
        Request savedRequest = requestRepository.save(request);
        return convertToRequestRO(savedRequest);
    }

    private void processEnrollmentChange(RequestDTO requestDTO, RequestEnrollDTO reDTO) throws UserNotFoundException, SubjectNotFoundException, SemesterNotFoundException, EnrollmentNotFoundException {
        switch (requestDTO.getRequestType()) {
            case ACCEPT://dean change enrollment status to ACCEPTED from PENDING when user cant do it
                enrollmentService.updateEnrollmentForUser(enrollDTOBuilder(reDTO, EnrollStatus.ACCEPTED));
                break;
            case ADD: //dean update  enrollment for user when user demands it - from status PROPOSED to PENDING
                enrollmentService.updateEnrollmentForUser(enrollDTOBuilder(reDTO, EnrollStatus.PENDING));
                break;
            case DELETE: //dean update enrollment status to rejected  for user when user demands it (simply discards enrollment)
                enrollmentService.updateEnrollmentForUser(enrollDTOBuilder(reDTO, EnrollStatus.REJECTED));
                break;
            default:
                throw new IllegalArgumentException("Invalid request type");
        }
    }

    private void notifyDeans() {
        userRepository.findAll(UserSpecifications.hasRoleDean())
                .forEach(user -> mailService.sendNotification(user.getEmail()));
    }

    private EnrollDTO enrollDTOBuilder(RequestEnrollDTO reDTO, EnrollStatus status){
        return EnrollDTO.builder()
                .userId(reDTO.getUserId())
                .subjectId(reDTO.getSubjectId())
                .enrollStatus(status)
                .build();
    }


    private RequestEnrollRO convertToRequestEnrollRO(RequestEnroll requestEnroll) {
        UserDTO userDTO = modelMapper.map(requestEnroll.getEnrollment().getEnrollStudent(), UserDTO.class);
        SubjectDTO subjectDTO = modelMapper.map(requestEnroll.getEnrollment().getEnrollSubject(), SubjectDTO.class);

        return RequestEnrollRO.builder()
                .requestEnrollId(requestEnroll.getRequestEnrollId())
                .requestStatus(requestEnroll.getRequestStatus())
                .user(userDTO)
                .subject(subjectDTO)
                .build();
    }

    private RequestRO convertToRequestRO(Request request) {
        Set<RequestEnrollRO> requestEnrollROs = request.getRequestEnrollment().stream()
                .map(this::convertToRequestEnrollRO)
                .collect(Collectors.toSet());
        return RequestRO.builder()
                .requestId(request.getRequestId())
                .senderId(request.getUser().getUserId())
                .description(request.getDescription())
                .submissionDate(request.getSubmissionDate().toString())
                .requestType(request.getRequestType())
                .requestEnrollments(requestEnrollROs)
                .build();
    }
}
