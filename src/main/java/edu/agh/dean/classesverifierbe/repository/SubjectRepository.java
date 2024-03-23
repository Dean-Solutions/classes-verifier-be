package edu.agh.dean.classesverifierbe.repository;

import edu.agh.dean.classesverifierbe.model.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubjectRepository extends CrudRepository<Subject, Long> {
}
