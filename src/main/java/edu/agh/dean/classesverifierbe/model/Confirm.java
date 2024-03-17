package edu.agh.dean.classesverifierbe.model;

import edu.agh.dean.classesverifierbe.model.enums.ConfirmStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "confirms")
public class Confirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long confirmId;

    private String semesterFullName;

    @Enumerated(EnumType.STRING)
    private ConfirmStatus confirmStatus;

    private LocalDateTime confirmDate;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
