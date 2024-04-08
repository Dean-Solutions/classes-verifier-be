package edu.agh.dean.classesverifierbe.repository;


import edu.agh.dean.classesverifierbe.model.Request;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends CrudRepository<Request, Long>, JpaSpecificationExecutor<Request> {

}
