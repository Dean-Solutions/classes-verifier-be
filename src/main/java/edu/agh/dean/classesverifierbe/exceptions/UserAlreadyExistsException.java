package edu.agh.dean.classesverifierbe.exceptions;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException(String attribute, String value,String container) {
        super("User with "+ attribute + " : " + value + " already exists in " + container);
    }

    public UserAlreadyExistsException(String attribute, String value) {
        super("User with "+ attribute + " : " + value + " already exists");
    }
    public UserAlreadyExistsException(String attribute) {
        super("User with given "+ attribute + " already exists");
    }
}
