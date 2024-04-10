package edu.agh.dean.classesverifierbe.specifications;

import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentSpecifications {
    public static Specification<Enrollment> withIndexNumber(String indexNumber){
        return (root, query, cb)  -> {
          if (indexNumber == null || indexNumber.trim().isEmpty()) return null;
          Join<Enrollment, User> userJoin = root.join("enrollStudent");
          return cb.equal(userJoin.get("indexNumber"), indexNumber);
        };
    }

    public static Specification<Enrollment> withUserId(Long userId){
        return (root, query, cb)  -> {
            if (userId == null) return null;
            return cb.equal(root.get("enrollStudent").get("userId"), userId);
        };
    }

    public static Specification<Enrollment> withSubjectId(Long subjectId){
        return (root, query, cb)  -> {
            if (subjectId == null) return null;
            return cb.equal(root.get("enrollSubject").get("subjectId"), subjectId);
        };
    }

    public static Specification<Enrollment> withSubjectName(String subjectName){
        return (root, query, cb)  -> {
            if (subjectName == null || subjectName.trim().isEmpty()) return null;
            Join<Enrollment, Subject> subjectJoin = root.join("enrollSubject");
            return cb.equal(subjectJoin.get("name"), subjectName);
        };
    }
    public static Specification<Enrollment> withSemesterId(Long semesterId){
        return (root, query, cb)  -> {
            if (semesterId == null) return null;
            Join<Enrollment, Semester> subjectJoin = root.join("semester");
            return cb.equal(subjectJoin.get("semesterId"), semesterId);
        };
    }

    public static Specification<Enrollment> withStatuses(String statuses){
        return (root, query, cb) -> {
            if (statuses == null || statuses.trim().isEmpty()) return null;

            String[] statusArray = statuses.split(",");
            List<Predicate> predicates = new ArrayList<>();
            for (String status : statusArray) {
                EnrollStatus enrollStatus;
                try {
                    enrollStatus = EnrollStatus.valueOf(status.toUpperCase().trim());
                    predicates.add(cb.equal(root.get("enrollStatus"), enrollStatus));
                } catch (IllegalArgumentException e) {
                    System.out.println("Inappriopriate status: " + status);
                }
            }
            Predicate orPredicate = cb.or(predicates.toArray(new Predicate[0]));
            return orPredicate;
        };
    }

}
