package edu.agh.dean.classesverifierbe.exceptions;

public class SubjectTagNotFoundException extends Exception {

    public SubjectTagNotFoundException(String attribute, String value,String container) {
        super("SubjectTag with "+ attribute + " : " + value + " not found in " + container);
    }

    public SubjectTagNotFoundException(String attribute, String value) {
        super("SubjectTag with "+ attribute + " : " + value + " not found");
    }
    public SubjectTagNotFoundException(String attribute) {
        super("SubjectTag with given "+ attribute + " not found");
    }


    public SubjectTagNotFoundException() {
        super("SubjectTag not found");
    }
}
