package edu.agh.dean.classesverifierbe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "subTagAssigns",
            joinColumns = @JoinColumn(name = "subjectId"),
            inverseJoinColumns = @JoinColumn(name = "subjectTagId"))
    private Set<SubjectTag> subjectTags;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "enrollSubject")
    private Set<Enrollment> enrollments;

}
