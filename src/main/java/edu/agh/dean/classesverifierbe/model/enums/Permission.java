package edu.agh.dean.classesverifierbe.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    STUDENT_READ("student:read"),
    STUDENT_UPDATE("student:update"),
    STUDENT_DELETE("student:delete"),
    STUDENT_CREATE("student:create"),
    STUDENT_REP_READ("student_rep:read"),
    STUDENT_REP_UPDATE("student_rep:update"),
    STUDENT_REP_DELETE("student_rep:delete"),
    STUDENT_REP_CREATE("student_rep:create"),
    DEAN_READ("dean:read"),
    DEAN_UPDATE("dean:update"),
    DEAN_DELETE("dean:delete"),
    DEAN_CREATE("dean:create");


    @Getter
    private final String permission;

}
