package edu.agh.dean.classesverifierbe.model.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    DEAN(
            Set.of(
                    Permission.TAG_READ,
                    Permission.TAG_UPDATE,
                    Permission.TAG_DELETE,
                    Permission.TAG_CREATE,
                    Permission.SUBJECT_READ,
                    Permission.SUBJECT_CREATE,
                    Permission.SUBJECT_UPDATE,
                    Permission.SUBJECT_DELETE,
                    Permission.USER_READ,
                    Permission.USER_CREATE,
                    Permission.USER_DELETE,
                    Permission.SEMESTER_READ,
                    Permission.SEMESTERS_READ,
                    Permission.SEMESTER_UPDATE,
                    Permission.SEMESTER_CREATE,
                    Permission.REQUEST_CREATE,
                    Permission.REQUEST_READ,
                    Permission.REQUEST_UPDATE,
                    Permission.USER_CHANGE_PASSWORD,
                    Permission.USER_CHANGE_PASSWORD_BY_FORCE
            )
    ),
    STUDENT_REP(
            Set.of(
                    Permission.TAG_READ,
                    Permission.SUBJECT_READ,
                    Permission.USER_READ,
                    Permission.SEMESTER_READ,
                    Permission.SEMESTERS_READ,
                    Permission.SEMESTER_UPDATE,
                    Permission.REQUEST_CREATE,
                    Permission.REQUEST_READ,
                    Permission.USER_CHANGE_PASSWORD
            )
    ),
    STUDENT(
            Set.of(
                    Permission.TAG_READ,
                    Permission.SUBJECT_READ,
                    Permission.SEMESTER_READ,
                    Permission.REQUEST_CREATE,
                    Permission.REQUEST_READ,
                    Permission.USER_CHANGE_PASSWORD
            )
    );
    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));//this.name() returns the name of the enum constant
        return authorities;
    }

    public static Role fromString(String roleStr) {
        try {
            return Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            return null; // lub obsłuż błąd inaczej
        }
    }
}
