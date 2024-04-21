package edu.agh.dean.classesverifierbe.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.agh.dean.classesverifierbe.RO.EnrollmentRO;
import edu.agh.dean.classesverifierbe.model.enums.EduPath;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;

    private String firstName;
    private String lastName;
    private String indexNumber;
    private String email;
    private Integer semester;
    private EduPath eduPath;
    private UserStatus status;
    private Role role;
    private String password;
}

