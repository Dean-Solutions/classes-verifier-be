package edu.agh.dean.classesverifierbe.dto;


import edu.agh.dean.classesverifierbe.model.enums.RequestEnrollStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestEnrollDTO {
    private Long requestEnrollId;//dean needs to know which request to accept or reject, student does not need to send this

    private Long semesterId; // if null, then the current semester is taken

    @NotNull(message = "UserId is required")
    private Long userId;

    @NotNull(message = "SubjectId is required")
    private Long subjectId;

    @NotNull(message = "RequestEnrollStatus is required")
    private RequestEnrollStatus requestStatus;

    private Long newSubjectId;
}
