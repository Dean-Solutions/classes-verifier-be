package edu.agh.dean.classesverifierbe.specifications;

import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class EnrollmentSpecifications {
    public static Specification<Enrollment> withIndexNumber(String indexNumber){
        return (root, query, cb)  -> {
          if (indexNumber == null || indexNumber.trim().isEmpty()) return null;
          Join<Enrollment, User> userJoin = root.join("enrollStudent");
          return cb.equal(userJoin.get("indexNumber"), indexNumber);
        };
    }

    public static Specification<Enrollment> withSubjectName(String subjectName){
        return (root, query, cb)  -> {
            if (subjectName == null || subjectName.trim().isEmpty()) return null;
            Join<Enrollment, Subject> subjectJoin = root.join("enrollSubject");
            return cb.equal(subjectJoin.get("subjectName"), subjectName);
        };
    }
    public static Specification<Enrollment> withSemesterId(Long semesterId){
        return (root, query, cb)  -> {
            if (semesterId == null) return null;
            Join<Enrollment, Semester> subjectJoin = root.join("semester");
            return cb.equal(subjectJoin.get("semesterId"), semesterId);
        };
    }

    public static Specification<Enrollment> withStatus(String status){
        return (root, query, cb)  -> {
            if (status == null || status.trim().isEmpty()) return null;
            EnrollStatus enrollStatus = EnrollStatus.valueOf(status.toUpperCase());
            return cb.equal(root.get("enrollStatus"), enrollStatus);
        };
    }
}
