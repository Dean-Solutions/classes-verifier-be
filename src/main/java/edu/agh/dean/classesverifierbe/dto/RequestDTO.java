package edu.agh.dean.classesverifierbe.dto;

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

    @NotNull(message = "Submission date is required")
    private LocalDateTime submissionDate;

    @NotNull(message = "Request type is required")
    private RequestType requestType;

    //Id of User that wants to add Request is required
    @NotNull(message = "SenderId is required")
    private Long senderId;
}
