package edu.agh.dean.classesverifierbe.repository;

import edu.agh.dean.classesverifierbe.model.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {
    Subject findByName(String name);
}
