package edu.agh.dean.classesverifierbe.specifications;

import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.RequestType;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequestSpecifications {

    public static Specification<Request> withRequestType(String requestTypesStr) {
        return (root, query, cb) -> {
            if (requestTypesStr == null || requestTypesStr.trim().isEmpty()) return null;
            CriteriaBuilder.In<String> inClause = cb.in(root.get("requestType"));
            Arrays.stream(requestTypesStr.split(",")).forEach(tag -> inClause.value(tag.trim()));

            return inClause;
        };
    }


    public static Specification<Request> withSenderId(String senderIdStr) {
        return (root, query, cb) -> {
            if (senderIdStr == null || senderIdStr.trim().isEmpty()) return null;

            Long senderId = Long.valueOf(senderIdStr);
            Join<Request, User> userJoin = root.join("user");
            return cb.equal(userJoin.get("userId"), senderId);
        };
    }
}
