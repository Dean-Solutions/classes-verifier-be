package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.RequestEnrollDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
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

import java.util.Objects;
import java.util.Optional;

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


    public RequestEnroll addRequestEnroll(Long requestId, RequestEnrollDTO requestEnrollDTO)
            throws UserNotFoundException,
            RequestNotFoundException,
            UserInsufficientPermissionException,
            RequestEnrollSingleRequestAlreadyExistsException,
            EnrollmentNotFoundException {
        RequestEnroll requestEnroll = new RequestEnroll();
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null) {
            throw new RequestNotFoundException(requestId);
        }

        User user = userRepository.findById(requestEnrollDTO.getSenderId()).orElse(null);
        if (user == null)
            throw new UserNotFoundException(requestEnrollDTO.getSenderId().toString());

        if(user.getRole() == STUDENT && request.getRequestType() != SINGLE)
            throw new UserInsufficientPermissionException(user.getRole(),  request.getRequestType());

        if(!request.getRequestEnrollment().isEmpty() && request.getRequestType() == SINGLE)
            throw new RequestEnrollSingleRequestAlreadyExistsException(requestId);


        Enrollment enrollment = enrollmentRepository.findById(requestEnrollDTO.getEnrollmentId()).orElse(null);

        //TODO do zmiany/zapytac
        if (enrollment == null) {
            if(requestEnrollDTO.getToDelete()){
                throw new EnrollmentNotFoundException();
            }
            else{
                //throw RequestEnrollTypeNotFound
            }
        }


        requestEnroll.setEnrollment(enrollment);
        requestEnroll.setRequest(request);
        requestEnroll.setRequestStatus(RequestEnrollStatus.PENDING);

        return requestEnrollRepository.save(requestEnroll);
    }

    public Optional<RequestEnroll> getRequestEnrollById(Long id) {
        return requestEnrollRepository.findById(id);
    }
}