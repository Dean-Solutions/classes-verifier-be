package edu.agh.dean.classesverifierbe.repository;

import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.enums.SemesterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
    @Query("SELECT s FROM Semester s WHERE s.deadline > CURRENT_DATE")
    Optional<Semester> findCurrentSemester();

    Optional<Semester> findByYearAndSemesterType(Integer year, SemesterType semesterType);
}

