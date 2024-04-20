package edu.agh.dean.classesverifierbe.exceptions;

public class PasswordsDoNotMatchException extends Exception{
    public PasswordsDoNotMatchException() {
        super("Passwords do not match!");
    }
}
