package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.RequestEnrollDTO;
import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.model.RequestEnroll;
import edu.agh.dean.classesverifierbe.model.enums.RequestEnrollStatus;
import edu.agh.dean.classesverifierbe.repository.RequestEnrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RequestEnrollService {
    @Autowired
    private RequestEnrollRepository requestEnrollRepository;

    public RequestEnroll addRequestEnroll(RequestEnrollDTO requestEnrollDTO) {
        RequestEnroll requestEnroll = toRequest(requestEnrollDTO);
        return requestEnrollRepository.save(requestEnroll);
    }

    private RequestEnroll toRequest(RequestEnrollDTO requestEnrollDTO) {
        RequestEnroll requestEnroll = new RequestEnroll();
        requestEnroll.setRequestStatus(RequestEnrollStatus.PENDING);
        requestEnroll.setEnrollment(requestEnrollDTO.getEnrollmentId());
        return requestEnroll;
    }

    public Optional<RequestEnroll> getRequestEnrollById(Long id) {
        return requestEnrollRepository.findById(id);
    }
}