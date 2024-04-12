package edu.agh.dean.classesverifierbe.controller;

import edu.agh.dean.classesverifierbe.RO.RequestRO;
import edu.agh.dean.classesverifierbe.dto.RequestDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/requests")
@PreAuthorize("hasAnyRole('DEAN', 'STUDENT_REP', 'STUDENT')")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('request:create')")
    public ResponseEntity<RequestRO> createRequest(@Valid @RequestBody RequestDTO requestDTO) throws UserNotFoundException, SemesterNotFoundException, SubjectNotFoundException, EnrollmentAlreadyExistException {
        RequestRO newRequest = requestService.createRequest(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRequest);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('request:update')")
    public ResponseEntity<RequestRO> updateRequest(@Valid @RequestBody RequestDTO requestDTO) throws RequestEnrollNotFoundException, UserNotFoundException, SubjectNotFoundException, SemesterNotFoundException, EnrollmentNotFoundException, EnrollmentAlreadyExistException {
        RequestRO updatedRequest = requestService.updateRequest(requestDTO);
        return ResponseEntity.ok(updatedRequest);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('request:read')")
    public ResponseEntity<Page<RequestRO>> getRequests(Pageable pageable,
                                                       @RequestParam(required = false) String requestType,
                                                       @RequestParam(required = false) String senderId) {
        Page<RequestRO> requests = requestService.getRequestByCriteria(pageable, requestType, senderId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('request:read')")
    public ResponseEntity<RequestRO> getRequestById(@PathVariable Long id) throws RequestNotFoundException {
        RequestRO requestRO = requestService.getRequestById(id);
        return ResponseEntity.ok(requestRO);
    }
}
