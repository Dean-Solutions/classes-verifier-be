package edu.agh.dean.classesverifierbe.model;

import edu.agh.dean.classesverifierbe.model.enums.SemesterType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "semesters")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long configId;

    @Enumerated(EnumType.STRING)
    private SemesterType semesterType;

    private Integer year;

    private LocalDateTime deadline;
}
