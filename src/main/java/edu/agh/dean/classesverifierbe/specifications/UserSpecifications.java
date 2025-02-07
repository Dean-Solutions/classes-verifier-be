package edu.agh.dean.classesverifierbe.specifications;

import edu.agh.dean.classesverifierbe.model.*;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.model.enums.UserStatus;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.*;

public class UserSpecifications {

    public static Specification<User> withTags(String tags) {
        return (root, query, cb) -> {
            if (tags == null || tags.trim().isEmpty()) return null;

            Join<User, Enrollment> enrollments = root.join("enrollments", JoinType.LEFT);
            Join<Enrollment, Subject> subjects = enrollments.join("enrollSubject", JoinType.LEFT);
            Join<Subject, SubjectTag> subjectTags = subjects.join("subjectTags", JoinType.LEFT);

            CriteriaBuilder.In<String> inClause = cb.in(subjectTags.get("name"));
            Arrays.stream(tags.split(",")).forEach(tag -> inClause.value(tag.trim()));

            return inClause;
        };
    }

    public static Specification<User> withSemester(Semester semester) {
        return (root, query, cb) -> {
            if (semester == null) return null;
            Join<User, Enrollment> enrollments = root.join("enrollments", JoinType.LEFT);

            return cb.equal(enrollments.get("semester"), semester);
        };
    }

    public static Specification<User> withSemester(Integer semester) {
        return (root, query, cb) -> semester == null ? null : cb.equal(root.get("semester"), semester);
    }

    public static Specification<User> withStatus(UserStatus status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<User> withName(String name) {
        return (root, query, cb) -> name == null || name.trim().isEmpty() ? null : cb.like(cb.lower(root.get("firstName")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<User> withLastName(String lastName) {
        return (root, query, cb) -> lastName == null || lastName.trim().isEmpty() ? null : cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<User> withIndex(String indexNumber) {
        return (root, query, cb) -> indexNumber == null || indexNumber.trim().isEmpty() ? null : cb.like(root.get("indexNumber"), "%" + indexNumber + "%");
    }



    public static Specification<User> hasRoleStudentOrStudentRep() {
        return (root, query, cb) -> cb.or(
                cb.equal(root.get("role"), Role.STUDENT),
                cb.equal(root.get("role"), Role.STUDENT_REP)
        );
    }

    public static Specification<User> hasRoleDean() {
        return (root, query, cb) -> cb.equal(root.get("role"), Role.DEAN);
    }

    public static Specification<User> withEnrollmentStatus(Set<EnrollStatus> enrollStatuses) {
        return (root, query, cb) -> {
            Join<Enrollment, User> userEnrollments = root.join("enrollments");
            CriteriaBuilder.In<EnrollStatus> inClause = cb.in(userEnrollments.get("enrollStatus"));
            enrollStatuses.forEach(inClause::value);
            return inClause;
        };
    }

    public static Optional<UserStatus> convertStringToUserStatus(String status) {
        if (status == null || status.equalsIgnoreCase("null")) {
            return Optional.empty();
        }
        try {
            return Optional.of(UserStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UserStatus value: " + status);
            return Optional.empty();
        }
    }

    public static Specification<User> byCriteria(String tags, String name, String lastName, String indexNumber, Integer semester, String status, Semester givenSemester) {
        UserStatus userStatus = convertStringToUserStatus(status).orElse(null);
        return Specification.where(hasRoleStudentOrStudentRep())
                .and(withTags(tags))
                .and(withSemester(givenSemester))
                .and(withName(name))
                .and(withLastName(lastName))
                .and(withIndex(indexNumber))
                .and(withSemester(semester))
                .and(withStatus(userStatus));
    }
}
