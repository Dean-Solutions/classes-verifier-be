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
                    Permission.STUDENT_READ,
                    Permission.STUDENT_UPDATE,
                    Permission.STUDENT_DELETE,
                    Permission.STUDENT_CREATE,
                    Permission.STUDENT_REP_READ,
                    Permission.STUDENT_REP_UPDATE,
                    Permission.STUDENT_REP_DELETE,
                    Permission.STUDENT_REP_CREATE,
                    Permission.DEAN_READ,
                    Permission.DEAN_UPDATE,
                    Permission.DEAN_DELETE,
                    Permission.DEAN_CREATE
            )
    ),
    STUDENT_REP(
            Set.of(
                    Permission.STUDENT_READ,
                    Permission.STUDENT_UPDATE,
                    Permission.STUDENT_DELETE,
                    Permission.STUDENT_CREATE,
                    Permission.STUDENT_REP_READ,
                    Permission.STUDENT_REP_UPDATE,
                    Permission.STUDENT_REP_DELETE,
                    Permission.STUDENT_REP_CREATE
            )
    ),
    STUDENT(
            Set.of(
                    Permission.STUDENT_READ,
                    Permission.STUDENT_UPDATE,
                    Permission.STUDENT_DELETE,
                    Permission.STUDENT_CREATE
            )
    )
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
}
