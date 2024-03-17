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
    //password need to be generated and sent in response!
    @Min(value = 1, message = "Semester must be greater than 0")
    private int semester;

    @NotNull(message = "User status is required")
    private UserStatus status;

    @NotNull(message = "Role is required")
    private Role role;
}
