package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.RequestDTO;
import edu.agh.dean.classesverifierbe.exceptions.RequestNotFoundException;
import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.model.enums.RequestStatus;
import edu.agh.dean.classesverifierbe.repository.RequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        request.setRequestStatus(RequestStatus.PENDING);
        request.setSubmissionDate(requestDTO.getSubmissionDate());
        request.setRequestType(requestDTO.getRequestType());
        return request;
    }

    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }

    public void deleteRequestById(Long id) throws RequestNotFoundException {
        try {
            requestRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RequestNotFoundException("ID", id.toString());
        }
    }

}
