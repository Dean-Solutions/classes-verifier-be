package edu.agh.dean.classesverifierbe.exceptions;

public class IncorrectPasswordException extends Exception{
    public IncorrectPasswordException() {
        super("Incorrect password!");
    }
}
