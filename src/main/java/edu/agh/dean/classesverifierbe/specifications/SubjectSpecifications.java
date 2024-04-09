package edu.agh.dean.classesverifierbe.specifications;

import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.SubjectTag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class SubjectSpecifications {

    public static Specification<Subject> withTags(String tags) {
        return (root, query, cb) -> {
            if (tags == null || tags.trim().isEmpty()) return null;

            Join<Subject, SubjectTag> subjectTags = root.join("subjectTags");

            String[] tagsArray = tags.split(",");
            CriteriaBuilder.In<String> inClause = cb.in(subjectTags.get("name"));
            for (String tag : tagsArray) {
                inClause.value(tag);
            }

            return cb.and(inClause);
        };
    }

    public static Specification<Subject> withNameLike(String name) {
        return (root, query, cb) -> {
            if (name == null || name.trim().isEmpty()) return null;

            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Subject> withSemester(Integer semester) {
        return (root, query, cb) -> {
            if (semester == null) return null;

            return cb.equal(root.get("semester"), semester);
        };
    }
}

