package edu.agh.dean.classesverifierbe.specifications;

import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.UserTag;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {

    public static Specification<User> withTag(String tagName) {
        return (root, query, cb) -> {
            if (StringUtils.isBlank(tagName)) return null;
            Join<User, UserTag> tagsJoin = root.join("userTags", JoinType.LEFT);
            return cb.like(tagsJoin.get("name"), "%" + tagName + "%");
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
    public static Specification<User> byCriteria(String tag, String name, String lastName, String indexNumber, Integer semester) {
        return Specification.where(withTag(tag))
                .and(hasRoleStudentOrStudentRep())
                .and(withName(name))
                .and(withLastName(lastName)).and(withIndex(indexNumber)).and(withSemester(semester));
    }
}
