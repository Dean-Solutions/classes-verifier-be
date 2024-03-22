package edu.agh.dean.classesverifierbe.exceptions;

public class UserTagAlreadyExistsException extends Exception {

    public UserTagAlreadyExistsException(String attribute, String value,String container) {
        super("User with "+ attribute + " : " + value + " already exists in " + container);
    }

    public UserTagAlreadyExistsException(String attribute, String value) {
        super("UserTag with "+ attribute + " : " + value + " already exists");
    }
    public UserTagAlreadyExistsException(String attribute) {
        super("UserTag with given "+ attribute + " already exists");
    }
}
