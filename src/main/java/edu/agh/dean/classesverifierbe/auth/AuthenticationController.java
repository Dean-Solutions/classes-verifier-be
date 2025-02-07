package edu.agh.dean.classesverifierbe.auth;


import edu.agh.dean.classesverifierbe.exceptions.NoPermissionException;
import edu.agh.dean.classesverifierbe.exceptions.UserAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) throws UserAlreadyExistsException, NoPermissionException{

        if(request.getRole() == null){
            request.setRole(Role.STUDENT);
        } else if (request.getRole() != Role.STUDENT) {
            throw new NoPermissionException("Only students can register");
        }
        AuthenticationResponse response = service.register(request);
        response.setPassword(null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate (
            @RequestBody AuthenticationRequest request
    ) throws UserNotFoundException {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

}