package edu.agh.dean.classesverifierbe.exceptions;

public class SubjectTagAlreadyExistsException extends Exception {

    public SubjectTagAlreadyExistsException(String attribute, String value,String container) {
        super("SubjectTag with "+ attribute + " : " + value + " already exists in " + container);
    }

    public SubjectTagAlreadyExistsException(String attribute, String value) {
        super("SubjectTag with "+ attribute + " : " + value + " already exists");
    }
    public SubjectTagAlreadyExistsException(String attribute) {
        super("SubjectTag with given "+ attribute + " already exists");
    }

    public SubjectTagAlreadyExistsException() {
        super("SubjectTag already exists");
    }
}