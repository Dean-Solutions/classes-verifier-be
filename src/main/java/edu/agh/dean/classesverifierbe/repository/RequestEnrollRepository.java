package edu.agh.dean.classesverifierbe.repository;

import edu.agh.dean.classesverifierbe.model.RequestEnroll;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestEnrollRepository extends CrudRepository<RequestEnroll, Long>, JpaSpecificationExecutor<RequestEnroll> {

    @Query("SELECT re FROM RequestEnroll re WHERE re.enrollment.enrollmentId = ?1")
    List<RequestEnroll> findByEnrollmentId(Long enrollmentId);
}
