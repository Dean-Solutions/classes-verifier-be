package edu.agh.dean.classesverifierbe.dto;

import edu.agh.dean.classesverifierbe.model.enums.RequestStatus;
import edu.agh.dean.classesverifierbe.model.enums.RequestType;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestDTO {
    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Request status is required")
    private RequestStatus requestStatus;

    @NotNull(message = "Submission date is required")
    private LocalDateTime submissionDate;

    @NotNull(message = "Request type is required")
    private RequestType requestType;

}
