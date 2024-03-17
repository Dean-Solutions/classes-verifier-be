package edu.agh.dean.classesverifierbe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "userTags")
public class UserTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userTagId;

    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, mappedBy = "userTags")
    private Set<User> users;
}
