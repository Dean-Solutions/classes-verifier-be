package edu.agh.dean.classesverifierbe.model;

import edu.agh.dean.classesverifierbe.model.enums.SemesterType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "semesters")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long semesterId;

    @Enumerated(EnumType.STRING)
    private SemesterType semesterType;

    private Integer year;

    private LocalDateTime deadline;

    private LocalDateTime reminderBeforeDeadline;
}
