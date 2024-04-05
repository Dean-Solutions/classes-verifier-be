package edu.agh.dean.classesverifierbe.specifications;

import edu.agh.dean.classesverifierbe.model.Request;
import org.springframework.data.jpa.domain.Specification;

public class RequestSpecifications {
    private static Specification<Object> withSenderId(String senderId) {

    }

    private static Specification<Object> withRequestType(String requestType) {

    }
    public static Specification<Request> byCriteria(String requestType, String senderId) {
        return Specification.where()
                .and(withRequestType(requestType))
                .and(withSenderId(senderId));
    }
}
