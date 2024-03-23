package edu.agh.dean.classesverifierbe.RO;


import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRO {
    private Long enrollmentId;
    private EnrollStatus enrollStatus;
    private SubjectRO enrollSubject;
}
