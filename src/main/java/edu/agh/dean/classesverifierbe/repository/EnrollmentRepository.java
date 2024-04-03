package edu.agh.dean.classesverifierbe.repository;

import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByEnrollStudentAndEnrollSubjectAndSemester(User user, Subject subject, Semester currentSemester);

    List<Enrollment> findAllByEnrollStudentAndSemester(User user, Semester semester);

    Optional<Enrollment> findEnrollmentByEnrollStudentAndEnrollSubjectAndSemester(User user, Subject subject, Semester semester);
}
