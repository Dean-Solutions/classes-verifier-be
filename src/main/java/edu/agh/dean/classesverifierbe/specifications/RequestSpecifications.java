package edu.agh.dean.classesverifierbe.specifications;

import edu.agh.dean.classesverifierbe.model.Request;
import org.springframework.data.jpa.domain.Specification;

public class RequestSpecifications {
    private static Specification<Request> withSenderId(String senderId) {
        return (root, query, cb) -> senderId == null || senderId.trim().isEmpty() ? null : cb.like(root.get("requestType"), "%" + senderId + "%");
    }

    private static Specification<Request> withRequestType(String requestType) {
        return (root, query, cb) -> requestType == null || requestType.trim().isEmpty() ? null : cb.like(root.get("requestType"), "%" + requestType + "%");
    }
    //TODO sprawdzić idk czy to ma jakis wiekszy sens
    public static Specification<Request> byCriteria(String requestType, String senderId) {
        return  withRequestType(requestType)
                .and(withSenderId(senderId));
    }
}
