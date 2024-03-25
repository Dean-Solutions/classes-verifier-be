package edu.agh.dean.classesverifierbe.exceptions;

public class EnrollmentAlreadyExistException extends Exception {

    public EnrollmentAlreadyExistException() {
        super("Enrollment already exists in db");
    }
}
