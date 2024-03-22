package edu.agh.dean.classesverifierbe.dto;

import edu.agh.dean.classesverifierbe.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterUserDto {

    @NotBlank
    private String indexNumber;

    @NotBlank
    private String hashPassword;

    @NotBlank
    private String email;

    public User toStudent() {
        User newStudent = new User();
        newStudent.setIndexNumber(indexNumber);
        newStudent.setHashPassword(hashPassword);
        newStudent.setEmail(email);
        return newStudent;
    }
}
