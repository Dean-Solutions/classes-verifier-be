package edu.agh.dean.classesverifierbe.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "subjects")
@EqualsAndHashCode(exclude = {"subjectTags", "enrollments"})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    private String name;

    private String description;

    private Integer semester;

    @PrePersist
    @PreUpdate
    private void prepareData(){
        if(this.semester == null){
            this.semester = 1;
        }
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "subTagAssigns",
            joinColumns = @JoinColumn(name = "subjectId"),
            inverseJoinColumns = @JoinColumn(name = "subjectTagId"))
    @JsonManagedReference
    private Set<SubjectTag> subjectTags;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "enrollSubject")
    @JsonBackReference
    private Set<Enrollment> enrollments;

}
