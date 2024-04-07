package edu.agh.dean.classesverifierbe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class SubjectDTO {

    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private Set<String> tagNames;


}
