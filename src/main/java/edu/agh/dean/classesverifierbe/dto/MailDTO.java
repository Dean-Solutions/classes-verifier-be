package edu.agh.dean.classesverifierbe.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MailDTO {

    @NotNull(message = "")
    private String to;

    @NotNull
    private String subject;

    @NotNull
    private String text;
}
