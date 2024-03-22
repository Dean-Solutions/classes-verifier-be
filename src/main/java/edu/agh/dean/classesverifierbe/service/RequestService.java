package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.RequestDTO;
import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.repository.RequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;

    public Request addRequest(RequestDTO requestDTO){
        Request request = toRequest(requestDTO);
        return requestRepository.save(request);
    }

    private Request toRequest(RequestDTO requestDTO) {
        Request request = new Request();
        request.setDescription(requestDTO.getDescription());
        request.setRequestStatus(requestDTO.getRequestStatus());
        request.setSubmissionDate(requestDTO.getSubmissionDate());
        request.setRequestType(requestDTO.getRequestType());
        return request;
    }
}
