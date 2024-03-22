package edu.agh.dean.classesverifierbe.repository;

import edu.agh.dean.classesverifierbe.model.RequestEnroll;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestEnrollRepository extends CrudRepository<RequestEnroll, Long>, JpaSpecificationExecutor<RequestEnroll> {

}
