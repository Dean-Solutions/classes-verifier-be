package edu.agh.dean.classesverifierbe.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Table(name = "enrollments")
@EqualsAndHashCode(exclude = {"enrollStudent", "enrollSubject", "semester", "requestEnrollment"})
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @Enumerated(EnumType.STRING)
    private EnrollStatus enrollStatus;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User enrollStudent;

    @ManyToOne
    @JoinColumn(name = "subjectId")
    @JsonManagedReference
    private Subject enrollSubject;


    @ManyToOne
    @JoinColumn(name = "semesterId")
    private Semester semester; // the semester in which we will complete or retake the subject

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "enrollment")
    private Set<RequestEnroll> requestEnrollment;

}
