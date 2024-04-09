package edu.agh.dean.classesverifierbe.exceptions;

public class RequestNotFoundException extends Exception{
    public RequestNotFoundException(String attribute, String value) {
        super("Request with "+ attribute + " : " + value + " not found");
    }

    public RequestNotFoundException(Long requestId) {
        super("Request with id: "+ requestId + " not found");
    }
}
