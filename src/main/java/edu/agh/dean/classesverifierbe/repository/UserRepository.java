package edu.agh.dean.classesverifierbe.repository;

import edu.agh.dean.classesverifierbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByIndexNumber(String indexNumber);

    Optional<User> findByIndexNumber(String indexNumber);
}