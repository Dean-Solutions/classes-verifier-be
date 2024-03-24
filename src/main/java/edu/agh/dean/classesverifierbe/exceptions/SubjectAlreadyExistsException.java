package edu.agh.dean.classesverifierbe.exceptions;

public class SubjectAlreadyExistsException extends Exception {

    public SubjectAlreadyExistsException(String attribute, String value,String container) {
        super("Subject with "+ attribute + " : " + value + " already exists in " + container);
    }

    public SubjectAlreadyExistsException(String attribute, String value) {
        super("Subject with "+ attribute + " : " + value + " already exists");
    }
    public SubjectAlreadyExistsException(String attribute) {
        super("Subject with given "+ attribute + " already exists");
    }

    public SubjectAlreadyExistsException() {
        super("Subject already exists");
    }
}
