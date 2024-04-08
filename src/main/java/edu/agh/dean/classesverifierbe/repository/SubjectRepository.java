package edu.agh.dean.classesverifierbe.repository;

import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {
    Subject findByName(String name);

    Page<Subject> findAll(Specification<Subject> spec, Pageable pageable);

    List<Subject> findBySemester(Integer semester);
}
