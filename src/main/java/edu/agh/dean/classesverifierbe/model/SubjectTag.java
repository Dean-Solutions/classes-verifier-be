package edu.agh.dean.classesverifierbe.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "subjectTags")
@EqualsAndHashCode(exclude = "subjects")
public class SubjectTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subjectTagId;

    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "subjectTags")
    @JsonBackReference
    private Set<Subject> subjects;

}
