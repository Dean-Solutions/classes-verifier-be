package edu.agh.dean.classesverifierbe.exceptions;

public class InvalidIndexException extends Exception {

    public InvalidIndexException(String value) {
        super("Invalid index: " + value);
    }

}
