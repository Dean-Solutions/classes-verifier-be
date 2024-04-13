package edu.agh.dean.classesverifierbe.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.agh.dean.classesverifierbe.model.enums.RequestEnrollStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "requestEnrolls")
@EqualsAndHashCode(exclude = {"request", "enrollment"})
public class RequestEnroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestEnrollId;

    @Enumerated(EnumType.STRING)
    private RequestEnrollStatus requestStatus;

    @ManyToOne
    @JoinColumn(name = "requestId")
    @JsonBackReference
    private Request request;

    @ManyToOne
    @JoinColumn(name = "enrollmentId")
    @JsonManagedReference
    private Enrollment enrollment;

    private Long newSubjectId;

}