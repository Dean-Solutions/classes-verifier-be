package edu.agh.dean.classesverifierbe.dto;

import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class MultiEnrollDTO {

    @NotNull(message = "subjectsToStudents map is required")
    private Map<Long, Set<Long>> subjectsToStudents;

    private Long semesterId;

    private EnrollStatus enrollStatus;

}
