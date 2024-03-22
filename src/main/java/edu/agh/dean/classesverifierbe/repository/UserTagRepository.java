package edu.agh.dean.classesverifierbe.repository;

import edu.agh.dean.classesverifierbe.model.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTagRepository extends JpaRepository<UserTag, Integer> {
    Optional<UserTag> findByName(String name);

}

