package edu.agh.dean.classesverifierbe.RO;

import edu.agh.dean.classesverifierbe.model.RequestEnroll;
import edu.agh.dean.classesverifierbe.model.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRO {
    private Long requestId;
    private Long senderId;
    private String description;
    private String submissionDate;
    private RequestType requestType;
    private Set<RequestEnrollRO> requestEnrollments;
}

