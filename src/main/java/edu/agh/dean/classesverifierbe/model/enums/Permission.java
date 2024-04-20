package edu.agh.dean.classesverifierbe.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    TAG_READ("tag:read"),
    TAG_UPDATE("tag:update"),
    TAG_DELETE("tag:delete"),
    TAG_CREATE("tag:create"),
    SUBJECT_READ("subject:read"),
    SUBJECT_CREATE("subject:create"),
    SUBJECT_UPDATE("subject:update"),
    SUBJECT_DELETE("subject:delete"),

    USER_DELETE("user:delete"),
    USER_CREATE("user:create"),
    USER_READ("user:read"),
    SEMESTER_READ("semester:read"),
    SEMESTER_CREATE("semester:create"),
    SEMESTER_UPDATE("semester:update"),
    SEMESTERS_READ("semesters:read"),
    REQUEST_READ("request:read"),
    REQUEST_CREATE("request:create"),
    REQUEST_UPDATE("request:update"),

    USER_CHANGE_PASSWORD("user:change-password"),
    USER_CHANGE_PASSWORD_BY_FORCE("user:change-password-forcefully");


    @Getter
    private final String permission;

}
