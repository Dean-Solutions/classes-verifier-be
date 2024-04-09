package edu.agh.dean.classesverifierbe.specifications;

import edu.agh.dean.classesverifierbe.model.Request;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.enums.RequestType;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class RequestSpecifications {

    public static Specification<Request> withRequestType(String requestTypeStr) {
        return (root, query, cb) -> {
            if (requestTypeStr == null || requestTypeStr.trim().isEmpty()) return null;

            RequestType requestType = RequestType.valueOf(requestTypeStr.toUpperCase());
            return cb.equal(root.get("requestType"), requestType);
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
