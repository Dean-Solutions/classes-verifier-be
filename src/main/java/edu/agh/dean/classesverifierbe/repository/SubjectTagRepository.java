package edu.agh.dean.classesverifierbe.repository;

import edu.agh.dean.classesverifierbe.model.SubjectTag;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface SubjectTagRepository extends CrudRepository<SubjectTag, Long> {
    @Query("SELECT st FROM SubjectTag st JOIN st.subjects s WHERE s.subjectId = :subjectId")
    Set<SubjectTag> findSubjectTagsBySubjectId(Long subjectId);
}