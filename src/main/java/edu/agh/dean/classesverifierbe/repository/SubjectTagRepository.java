package edu.agh.dean.classesverifierbe.repository;

import edu.agh.dean.classesverifierbe.model.SubjectTag;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SubjectTagRepository extends CrudRepository<SubjectTag, Long> {
    @Query("SELECT st FROM SubjectTag st JOIN st.subjects s WHERE s.subjectId = :subjectId")
    Set<SubjectTag> findSubjectTagsBySubjectId(Long subjectId);

    Optional<SubjectTag> findByName(String name);
}