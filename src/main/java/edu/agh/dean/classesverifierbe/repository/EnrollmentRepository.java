package edu.agh.dean.classesverifierbe.repository;

import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import edu.agh.dean.classesverifierbe.model.enums.EnrollStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByEnrollStudentAndEnrollSubjectAndSemester(User user, Subject subject, Semester currentSemester);

    List<Enrollment> findAllByEnrollStudentAndSemesterAndEnrollStatusIsIn(User user, Semester semester, Set<EnrollStatus> enrollStatuses);

    Optional<Enrollment> findEnrollmentByEnrollStudentAndEnrollSubjectAndSemester(User user, Subject subject, Semester semester);

    Page<Enrollment> findAll(Specification<Enrollment> spec, Pageable pageable);
}
