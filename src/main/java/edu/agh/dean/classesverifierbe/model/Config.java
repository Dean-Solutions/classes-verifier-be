package edu.agh.dean.classesverifierbe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "configs")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long configId;

    private String semesterFullName;

    private LocalDateTime deadline;
}
