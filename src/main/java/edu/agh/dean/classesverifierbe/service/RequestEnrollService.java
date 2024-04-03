package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.RequestEnrollDTO;
import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.model.RequestEnroll;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.RequestEnrollStatus;
import edu.agh.dean.classesverifierbe.repository.EnrollmentRepository;
import edu.agh.dean.classesverifierbe.repository.RequestEnrollRepository;
import edu.agh.dean.classesverifierbe.repository.RequestRepository;
import edu.agh.dean.classesverifierbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static edu.agh.dean.classesverifierbe.model.enums.RequestType.GROUP;
import static edu.agh.dean.classesverifierbe.model.enums.RequestType.SINGLE;
import static edu.agh.dean.classesverifierbe.model.enums.Role.STUDENT;

@Service
public class RequestEnrollService {
    @Autowired
    private RequestEnrollRepository requestEnrollRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private UserRepository userRepository;


    public RequestEnroll addRequestEnroll(Long requestId, RequestEnrollDTO requestEnrollDTO) throws UserNotFoundException {
        RequestEnroll requestEnroll = new RequestEnroll();
        Request request = requestRepository.findById(requestId).orElse(null);
//  TODO  add Exception when Request not Created
//        if (request == null) {
//            throw new RequestNotFoundException("Current r not found");
//        }

        User user = userRepository.findById(requestEnrollDTO.getSenderId()).orElse(null);
//  TODO Check if user that sent the request is the same user^
        if (user == null)
            throw new UserNotFoundException(requestEnrollDTO.getSenderId().toString());

//  TODO  add Permission Exceptions
//        if(user.getRole() == STUDENT && request.getRequestType() != SINGLE)
//            throw new UserInsufficientPermissionException();

//  TODO  add Exeption when request already created
//        if(request.getRequestEnrollment().size() > 0 && request.getRequestType() == SINGLE)
//            throw new RequestEnrollSingleRequestAlreadyExists();


        Enrollment enrollment = enrollmentRepository.findById(requestEnrollDTO.getEnrollmentId()).orElse(null);
//  TODO if RequestEnroll Type DELETE it should exist first else it shouldn't
//        if (enrollment == null) {
//            throw new RequestNotFoundException("Current r not found");
//        }


        requestEnroll.setEnrollment(enrollment);
        requestEnroll.setRequest(request);
        requestEnroll.setRequestStatus(RequestEnrollStatus.PENDING);

//  TODO Here add sth with ReqEnroll type?
        return requestEnrollRepository.save(requestEnroll);
    }

    public Optional<RequestEnroll> getRequestEnrollById(Long id) {
        return requestEnrollRepository.findById(id);
    }
}