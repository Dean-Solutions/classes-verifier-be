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
//        if (request == null) {
//            throw new RequestNotFoundException("Current r not found");
//        }

        User user = userRepository.findById(requestEnrollDTO.getSenderId()).orElse(null);
        if (user == null)
            throw new UserNotFoundException(requestEnrollDTO.getSenderId().toString());

//        if (user != request.getUser())
//            //TODO
//            //            throw new uprawnieniaERROR;


        //tutaj patrzymy czy request stworzony single czy group
        //jak single i jest juz jakis podpiety pod ten request to error

        Enrollment enrollment = enrollmentRepository.findById(requestEnrollDTO.getEnrollmentId()).orElse(null);
//        if (enrollment == null) {
//            throw new RequestNotFoundException("Current r not found");
//        }
        requestEnroll.setEnrollment(enrollment);
        requestEnroll.setRequest(request);
        requestEnroll.setRequestStatus(RequestEnrollStatus.PENDING);

        return requestEnrollRepository.save(requestEnroll);
    }

    public Optional<RequestEnroll> getRequestEnrollById(Long id) {
        return requestEnrollRepository.findById(id);
    }
}