package edu.agh.dean.classesverifierbe.model;


import edu.agh.dean.classesverifierbe.model.enums.RequestEnrollStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "requestEnrolls")
public class RequestEnroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestEnrollId;

    @Enumerated(EnumType.STRING)
    private RequestEnrollStatus requestStatus;

    @ManyToOne
    @JoinColumn(name = "requestId")
    private Request request;

    @ManyToOne
    @JoinColumn(name = "enrollmentId")
    private Enrollment enrollment;



}
