package edu.agh.dean.classesverifierbe.exceptions;

import edu.agh.dean.classesverifierbe.model.enums.RequestType;
import edu.agh.dean.classesverifierbe.model.enums.Role;

public class UserInsufficientPermissionException extends Exception{
    public UserInsufficientPermissionException(Role role, RequestType requestType){
        super("User with role "+ role + " doesn't have rights to create " + requestType);
    }
}
