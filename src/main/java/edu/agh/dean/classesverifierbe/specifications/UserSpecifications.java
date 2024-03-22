package edu.agh.dean.classesverifierbe.specifications;

import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.UserTag;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import edu.agh.dean.classesverifierbe.model.enums.UserStatus;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserSpecifications {

    public static Specification<User> withTag(String[] tagNames) {
        return (root, query, cb) -> {
            if (tagNames == null || tagNames.length == 0) return null;
            Join<User, UserTag> tagsJoin = root.join("userTags", JoinType.LEFT);
            List<Predicate> predicates = new ArrayList<>();
            for (String tagName : tagNames) {
                if (StringUtils.isNotBlank(tagName)) {
                    predicates.add(cb.like(tagsJoin.get("name"), "%" + tagName.trim() + "%"));
                }
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
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

    public static Specification<User> withSemester(Integer semester) {
        return (root, query, cb) -> semester == null ? null : cb.equal(root.get("semester"), semester);
    }

    public static Specification<User> hasRoleStudentOrStudentRep() {
        return (root, query, cb) -> cb.or(
                cb.equal(root.get("role"), Role.STUDENT),
                cb.equal(root.get("role"), Role.STUDENT_REP)
        );
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

    public static Specification<User> byCriteria(String tags, String name, String lastName, String indexNumber, Integer semester, String status) {
        UserStatus userStatus = convertStringToUserStatus(status).orElse(null);
        String[] tagArray = tags != null ? tags.split(",") : new String[]{};
        return Specification.where(withTag(tagArray))
                .and(hasRoleStudentOrStudentRep())
                .and(withName(name))
                .and(withLastName(lastName))
                .and(withIndex(indexNumber))
                .and(withSemester(semester))
                .and(withStatus(userStatus));
    }
}
