package edu.agh.dean.classesverifierbe.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollDTO {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "messageId is required")
    private Long subjectId;

}
