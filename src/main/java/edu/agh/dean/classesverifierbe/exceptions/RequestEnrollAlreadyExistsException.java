package edu.agh.dean.classesverifierbe.exceptions;

public class RequestEnrollAlreadyExistsException extends Exception{
    public RequestEnrollAlreadyExistsException() {
        super("RequestEnroll already exists in db");
    }

    public RequestEnrollAlreadyExistsException(String message) {
        super(message);
    }
}
