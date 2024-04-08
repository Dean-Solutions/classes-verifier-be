package edu.agh.dean.classesverifierbe.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.agh.dean.classesverifierbe.model.enums.RequestType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "requests")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"user", "requestEnrollment"})
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    private String description;


    private LocalDateTime submissionDate;

    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "request")
    @JsonManagedReference
    private Set<RequestEnroll> requestEnrollment;
}
