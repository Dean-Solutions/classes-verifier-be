package edu.agh.dean.classesverifierbe.model;


import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "enrollments")
public class Enrollment {

    @Id
    private Long enrollmentId;

    @Enumerated(EnumType.STRING)
    private EnrollStatus enrollStatus;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User enrollStudent;

    @ManyToOne
    @JoinColumn(name = "subjectId")
    private Subject enrollSubject;


    @ManyToOne
    @JoinColumn(name = "semesterId")
    private Semester semester; // the semester in which we will complete or retake the subject


}
