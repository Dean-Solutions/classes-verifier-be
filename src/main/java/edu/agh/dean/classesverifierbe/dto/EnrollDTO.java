package edu.agh.dean.classesverifierbe.dto;


import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollDTO {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "subjectId is required")
    private Long subjectId;

    private EnrollStatus enrollStatus;

}
