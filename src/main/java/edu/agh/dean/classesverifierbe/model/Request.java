package edu.agh.dean.classesverifierbe.model;

import edu.agh.dean.classesverifierbe.model.enums.RequestStatus;
import edu.agh.dean.classesverifierbe.model.enums.RequestType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "requests")
@Data
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    private String description;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    private LocalDateTime submissionDate;

    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "request")
    private Set<RequestEnroll> requestEnrollment;
}
