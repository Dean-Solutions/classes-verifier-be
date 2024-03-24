package edu.agh.dean.classesverifierbe.dto;

import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Request;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestEnrollDTO {

    @NotBlank(message = "EnrollId is required")
    private Enrollment enrollmentId;
}
