package edu.agh.dean.classesverifierbe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="Semester")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long semesterId;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

}
