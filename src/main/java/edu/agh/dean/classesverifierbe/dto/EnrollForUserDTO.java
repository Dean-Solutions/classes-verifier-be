package edu.agh.dean.classesverifierbe.dto;

import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class EnrollForUserDTO {

    private Long userId;

    private String index;

    private Long semesterId;

    @NotNull(message = "enrollStatuses are required")
    private Set<EnrollStatus> enrollStatuses;

}
