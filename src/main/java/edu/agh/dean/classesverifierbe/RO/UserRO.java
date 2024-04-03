package edu.agh.dean.classesverifierbe.RO;

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
@NoArgsConstructor
@AllArgsConstructor
public class UserRO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String indexNumber;
    private String email;
    private String hashPassword;
    private Integer semester;
    private EduPath eduPath;
    private UserStatus status;
    private Role role;
    private Set<EnrollmentRO> enrollments;

    public void hidePassword() {
        this.hashPassword = null;
    }
}
