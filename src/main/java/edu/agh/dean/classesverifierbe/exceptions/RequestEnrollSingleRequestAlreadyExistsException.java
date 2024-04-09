package edu.agh.dean.classesverifierbe.exceptions;

public class RequestEnrollSingleRequestAlreadyExistsException extends Exception{
    public RequestEnrollSingleRequestAlreadyExistsException(Long requestId) {
        super("Single request with id: "+ requestId + "already has an request enroll");
    }
}
