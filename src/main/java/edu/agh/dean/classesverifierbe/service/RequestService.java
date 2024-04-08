package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.RO.RequestRO;
import edu.agh.dean.classesverifierbe.dto.EnrollDTO;
import edu.agh.dean.classesverifierbe.dto.RequestDTO;
import edu.agh.dean.classesverifierbe.dto.RequestEnrollDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.*;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import edu.agh.dean.classesverifierbe.repository.RequestEnrollRepository;
import edu.agh.dean.classesverifierbe.repository.RequestRepository;

import edu.agh.dean.classesverifierbe.repository.UserRepository;
import edu.agh.dean.classesverifierbe.specifications.RequestSpecifications;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static edu.agh.dean.classesverifierbe.model.enums.RequestEnrollStatus.ACCEPTED;
import static edu.agh.dean.classesverifierbe.model.enums.RequestEnrollStatus.PENDING;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RequestEnrollRepository requestEnrollRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private SemesterService semesterService;
    public RequestRO getRequestById(Long id) throws RequestNotFoundException{
        Request request = getRawRequestById(id);
        return convertToRequestRO(request);
    }

    private RequestRO convertToRequestRO(Request request) {
        return modelMapper.map(request, RequestRO.class);
    }

    public Request getRawRequestById(Long id) throws RequestNotFoundException {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException("id", id.toString()));
    }

    public Page<RequestRO> getRequestByCriteria(Pageable pageable, String requestType, String senderId) {
        Page<Request> requests = requestRepository.findAll(RequestSpecifications.byCriteria(requestType, senderId), pageable);
        List<RequestRO> RequestROs = requests.getContent().stream()
                .map(this::convertToRequestRO)
                .collect(Collectors.toList());
        return new PageImpl<>(RequestROs, pageable, requests.getTotalElements());
    }


    @Transactional
    public Request createRequest(RequestDTO requestDTO) throws UserNotFoundException, SemesterNotFoundException, SubjectNotFoundException, EnrollmentAlreadyExistException {
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
            Enrollment enrollment = enrollmentService.getEnrollmentByUserIdAndSubjectIdAndSemesterId(sender.getUserId(), reDTO.getSubjectId(), semester.getSemesterId());
//          (****)  if(enrollment == null){
//                    enrollmentService.assignEnrollmentForUser(EnrollDTO.builder()
//                            .userId(reDTO.getUserId())
//                            .subjectId(reDTO.getSubjectId())
//                            .enrollStatus(EnrollStatus.PROPOSED)
//                            .build());
//
//            } --> maybe it is not needed at all? depends on that, if user will ever use request from enroll. Dean only see requestEnroll directly from requests objects
            RequestEnroll requestEnroll = RequestEnroll.builder()
                    .request(request)
                    .enrollment(enrollment)
                    .requestStatus(PENDING)
                    .build();
            request.getRequestEnrollment().add(requestEnroll);
        }
        return requestRepository.save(request);
    }
    @Transactional
    public Request updateRequest(RequestDTO requestDTO) throws RequestEnrollNotFoundException, UserNotFoundException, SubjectNotFoundException, SemesterNotFoundException, EnrollmentNotFoundException, EnrollmentAlreadyExistException{
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

        return requestRepository.save(request);
    }

    private void processEnrollmentChange(RequestDTO requestDTO, RequestEnrollDTO reDTO) throws UserNotFoundException, SubjectNotFoundException, SemesterNotFoundException, EnrollmentNotFoundException, EnrollmentAlreadyExistException {
        switch (requestDTO.getRequestType()) {
            case ACCEPT://dean change enrollment status to ACCEPTED from PENDING when user cant do it
                enrollmentService.updateEnrollmentForUser(enrollDTOBuilder(reDTO, EnrollStatus.ACCEPTED));
                break;
            case ADD: //dean add new enrollment for user when user demands it - (****) see line 50
                enrollmentService.assignEnrollmentForUser(enrollDTOBuilder(reDTO, EnrollStatus.PENDING));
                break;
            case DELETE: //dean update enrollment status to rejected  for user when user demands it (simply discards enrollment)
                enrollmentService.updateEnrollmentForUser(enrollDTOBuilder(reDTO, EnrollStatus.REJECTED));
                break;
            default:
                throw new IllegalArgumentException("Invalid request type");
        }
    }

    private EnrollDTO enrollDTOBuilder(RequestEnrollDTO reDTO, EnrollStatus status){
        return EnrollDTO.builder()
                .userId(reDTO.getUserId())
                .subjectId(reDTO.getSubjectId())
                .enrollStatus(status)
                .build();
    }
}
