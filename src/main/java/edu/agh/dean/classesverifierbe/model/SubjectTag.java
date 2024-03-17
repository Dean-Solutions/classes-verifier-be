package edu.agh.dean.classesverifierbe.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "subjectTags")
public class SubjectTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subjectTagId;

    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, mappedBy = "subjectTags")
    private Set<Subject> subjects;

}
