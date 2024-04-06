package edu.agh.dean.classesverifierbe.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestEnrollDTO {

    @NotNull(message = "UserId is required")
    private Long userId;

    @NotNull(message = "SubjectId is required")
    private Long subjectId;

    //Id of User that wants to add RequestEnroll to request is required// must be? ask
    @NotNull(message = "SenderId is required")
    private Long senderId;
}
