package edu.agh.dean.classesverifierbe.exceptions;

public class UserTagNotFoundException extends Exception{

    public UserTagNotFoundException(String attribute, String value,String container) {
        super("UserTag with "+ attribute + " : " + value + " not found in " + container);
    }
    public UserTagNotFoundException(String attribute, String value) {
        super("UserTag with "+ attribute + " : " + value + " not found");
    }
    public UserTagNotFoundException(String attribute) {
        super("UserTag with given "+ attribute + " not found");
    }
}


