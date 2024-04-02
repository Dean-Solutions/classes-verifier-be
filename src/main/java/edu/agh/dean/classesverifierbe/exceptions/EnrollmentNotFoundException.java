package edu.agh.dean.classesverifierbe.exceptions;

public class EnrollmentNotFoundException extends Exception {
    public EnrollmentNotFoundException() {
        super("Enrollment not found in db");
    }

}
