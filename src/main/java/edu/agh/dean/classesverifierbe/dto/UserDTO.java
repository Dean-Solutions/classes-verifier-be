package edu.agh.dean.classesverifierbe.dto;

import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.model.enums.UserStatus;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Index number is required")
    private String indexNumber;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;

    private Integer semester;

    private UserStatus status;

    private Role role;

    private Long userId;


}
