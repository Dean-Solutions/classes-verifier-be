package edu.agh.dean.classesverifierbe.exceptions;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(String attribute, String value,String container) {
        super("User with "+ attribute + " : " + value + " not found in " + container);
    }

    public UserNotFoundException(String attribute, String value) {
        super("User with "+ attribute + " : " + value + " not found");
    }
    public UserNotFoundException(String attribute) {
        super("User with given "+ attribute + " not found");
    }

}
