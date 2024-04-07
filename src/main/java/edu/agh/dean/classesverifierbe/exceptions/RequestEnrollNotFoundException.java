package edu.agh.dean.classesverifierbe.exceptions;

public class RequestEnrollNotFoundException extends Exception{
    public RequestEnrollNotFoundException(String attribute, String value) {
        super("Request Enroll with "+ attribute + " : " + value + " not found");
    }

    public RequestEnrollNotFoundException(Long requestId) {
        super("Request Enroll with id: "+ requestId + " not found");
    }
}
