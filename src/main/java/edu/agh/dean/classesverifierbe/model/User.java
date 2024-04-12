package edu.agh.dean.classesverifierbe.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.agh.dean.classesverifierbe.model.enums.EduPath;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.model.enums.UserStatus;
import edu.agh.dean.classesverifierbe.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"requests", "enrollments"})
public class User implements UserDetails {

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
    private Role role = Role.STUDENT;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonManagedReference
    private Set<Request> requests;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "enrollStudent")
    private Set<Enrollment> enrollments;


    @OneToMany(mappedBy = "user")
    private List<Token>tokens;

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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return hashPassword;
    }

}
