package edu.agh.dean.classesverifierbe.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"userTags", "userRequests", "enrollments", "confirms"})
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
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STUDENT;;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "userTagAssigns",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "userTagId"))
    @JsonManagedReference
    private Set<UserTag> userTags;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<UserRequest> userRequests;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "enrollStudent")
    private Set<Enrollment> enrollments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Confirm> confirms;

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
