package edu.agh.dean.classesverifierbe.exceptions;

public class SubjectNotFoundException extends Exception {

    public SubjectNotFoundException(String attribute, String value,String container) {
        super("Subject with "+ attribute + " : " + value + " not found in " + container);
    }

    public SubjectNotFoundException(String attribute, String value) {
        super("Subject with "+ attribute + " : " + value + " not found");
    }
    public SubjectNotFoundException(String attribute) {
        super("Subject with given "+ attribute + " not found");
    }

    public SubjectNotFoundException() {
        super("Subject not found");
    }
}
