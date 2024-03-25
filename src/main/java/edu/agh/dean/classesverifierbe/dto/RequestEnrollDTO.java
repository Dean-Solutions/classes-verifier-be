package edu.agh.dean.classesverifierbe.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestEnrollDTO {

    @NotNull(message = "EnrollId is required")
    private Long enrollmentId;

    //Id of User that wants to add Enrollment to the Request is required
    @NotNull(message = "SenderId is required")
    private Long senderId;
}
