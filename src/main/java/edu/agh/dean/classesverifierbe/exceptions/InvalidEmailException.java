package edu.agh.dean.classesverifierbe.exceptions;

public class InvalidEmailException extends Exception {
    public InvalidEmailException(String value) {
        super("Invalid email: " + value);
    }
}
