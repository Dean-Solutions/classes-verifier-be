package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.RO.RequestRO;
import edu.agh.dean.classesverifierbe.dto.RequestDTO;
import edu.agh.dean.classesverifierbe.exceptions.RequestNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.RequestStatus;
import edu.agh.dean.classesverifierbe.model.enums.RequestType;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.repository.RequestRepository;

import edu.agh.dean.classesverifierbe.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Request addRequest(RequestDTO requestDTO) throws UserNotFoundException {
        // TODO Czy tutaj powinna nastąpić weryfikacja że on to on?
        //  Może stworzyć jakiś verify service?
        User user = userRepository.findById(requestDTO.getSenderId()).orElse(null);
        if (user == null)
            throw new UserNotFoundException(requestDTO.getSenderId().toString());
        //Student shouldn't be able to create GROUP request
        if (user.getRole() == Role.STUDENT && requestDTO.getRequestType() != RequestType.SINGLE){
            //TODO
            //            throw new UserInsufficientPermissionException();
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

    public RequestRO deleteRequestById(Long id) throws RequestNotFoundException {
        RequestRO request = getRequestById(id);
        requestRepository.deleteById(id);
        return request;
    }
}
