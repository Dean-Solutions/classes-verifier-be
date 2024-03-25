package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.dto.RequestDTO;
import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.RequestStatus;
import edu.agh.dean.classesverifierbe.model.enums.RequestType;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.repository.RequestRepository;

import edu.agh.dean.classesverifierbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    public Request addRequest(RequestDTO requestDTO) throws UserNotFoundException {
        // TODO Czy tutaj powinna nastąpić weryfikacja że on to on?
        // Może stworzyć jakiś verify service?
        User user = userRepository.findById(requestDTO.getSenderId()).orElse(null);
        if (user == null)
            throw new UserNotFoundException(requestDTO.getSenderId().toString());
        //Student shouldn't be able to create GROUP request
        if (user.getRole() == Role.STUDENT && requestDTO.getRequestType() == RequestType.GROUP){
            //TODO
            //            throw new uprawnieniaERROR;
        }

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

    // TODO deleting requests
//    public void deleteRequestById(Long id) throws RequestNotFoundException {
//        try {
//            requestRepository.deleteById(id);
//        } catch (EmptyResultDataAccessException e) {
//            throw new RequestNotFoundException("ID", id.toString());
//        }
//    }
}
