package edu.agh.dean.classesverifierbe.specifications;

import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.Role;
import org.springframework.data.jpa.domain.Specification;



public class UserSpecifications {

    public static Specification<User> withTag(String tag) {
        return (root, query, cb) -> {
            if (tag == null || tag.isEmpty()) return null;
            return cb.isMember(tag, root.join("userTags").get("name"));
        };
    }

    public static Specification<User> withName(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(root.get("firstName"), "%" + name + "%");
    }

    public static Specification<User> withLastName(String lastName) {
        return (root, query, cb) -> lastName == null ? null : cb.like(root.get("lastName"), "%" + lastName + "%");
    }

    public static Specification<User> withIndex(String indexNumber) {
        return (root, query, cb) -> indexNumber == null ? null : cb.equal(root.get("indexNumber"), indexNumber);
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

