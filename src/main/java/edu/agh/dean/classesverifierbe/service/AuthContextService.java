package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.configuration.JwtService;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.stream.Collectors;

@Service
public class AuthContextService {

    public Role getCurrentRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
            return authentication.getAuthorities()
                    .stream()
                    .map(Object::toString)
                    .filter(auth -> auth.startsWith("ROLE_"))
                    .findFirst()
                    .map(roleStr -> roleStr.replace("ROLE_", ""))
                    .map(Role::fromString).orElse(null);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        User user = (User) authentication.getPrincipal();
        return user;
    }

    public User getUserFromPrincipal(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return user;
    }

}
