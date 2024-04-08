package edu.agh.dean.classesverifierbe.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.agh.dean.classesverifierbe.model.enums.EduPath;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"requests", "enrollments"})

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String firstName;

    private String lastName;

    private String indexNumber;

    private String email;

    private String hashPassword;

    private Integer semester = 1;

    @Enumerated(EnumType.STRING)
    private EduPath eduPath;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STUDENT;;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonManagedReference
    private Set<Request> requests;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "enrollStudent")
    private Set<Enrollment> enrollments;

    @PrePersist
    @PreUpdate
    private void prepareData(){
        if (role == null) {
            role = Role.STUDENT;
        }
        if(status == null){
            status = UserStatus.ACTIVE;
        }
        if(semester == null){
            semester = 1;
        }

    }


}
