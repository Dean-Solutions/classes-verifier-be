package edu.agh.dean.classesverifierbe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDTO {

    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private Integer semester;
    private Set<String> tagNames;
    private Long subjectId;


}
