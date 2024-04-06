package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.RO.RequestRO;
import edu.agh.dean.classesverifierbe.dto.RequestDTO;
import edu.agh.dean.classesverifierbe.exceptions.RequestNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.UserInsufficientPermissionException;
import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.RequestStatus;
import edu.agh.dean.classesverifierbe.model.enums.RequestType;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.repository.RequestRepository;

import edu.agh.dean.classesverifierbe.repository.UserRepository;
import edu.agh.dean.classesverifierbe.specifications.RequestSpecifications;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Request addRequest(RequestDTO requestDTO) throws UserNotFoundException, UserInsufficientPermissionException {
        // TODO Czy tutaj powinna nastąpić weryfikacja że on to on?
        //  Może stworzyć jakiś verify service?
        User user = userRepository.findById(requestDTO.getSenderId()).orElse(null);
        if (user == null)
            throw new UserNotFoundException(requestDTO.getSenderId().toString());

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

    public Page<RequestRO> getRequestByCriteria(Pageable pageable, String requestType, String senderId) {
        Page<Request> requests = requestRepository.findAll(RequestSpecifications.byCriteria(requestType, senderId), pageable);
        List<RequestRO> RequestROs = requests.getContent().stream()
                .map(this::convertToRequestRO)
                .collect(Collectors.toList());
        return new PageImpl<>(RequestROs, pageable, requests.getTotalElements());
    }
}
