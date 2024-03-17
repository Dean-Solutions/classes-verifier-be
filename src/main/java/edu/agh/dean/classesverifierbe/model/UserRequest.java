package edu.agh.dean.classesverifierbe.model;

import edu.agh.dean.classesverifierbe.model.enums.RequestStatus;
import edu.agh.dean.classesverifierbe.model.enums.RequestType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "userRequests")
@Data
public class UserRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRequestId;

    private String description;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    private LocalDateTime submissionDate;

    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subjectId")
    private Subject subject;

}
