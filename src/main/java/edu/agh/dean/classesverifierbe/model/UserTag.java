package edu.agh.dean.classesverifierbe.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;


import java.util.Objects;
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
    @JsonBackReference
    private Set<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTag userTag = (UserTag) o;
        return userTagId == userTag.userTagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userTagId);
    }


}
