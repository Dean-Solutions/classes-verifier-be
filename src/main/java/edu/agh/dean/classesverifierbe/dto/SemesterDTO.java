package edu.agh.dean.classesverifierbe.dto;

import edu.agh.dean.classesverifierbe.model.enums.SemesterType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
public class SemesterDTO {

    @NotNull(message = "Semester type is required")
    private SemesterType semesterType;

    @NotNull(message = "Year is required")
    private Integer year;

    @NotNull(message = "Deadline is required")
    @Future(message = "Deadline must be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deadline;

    @NotNull(message = "ReminderBeforeDeadline is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime reminderBeforeDeadline;

}
