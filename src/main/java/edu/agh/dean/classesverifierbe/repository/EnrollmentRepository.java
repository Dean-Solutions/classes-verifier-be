package edu.agh.dean.classesverifierbe.repository;

import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends CrudRepository<Enrollment, Long> {
    boolean existsByEnrollStudentAndEnrollSubjectAndSemester(User user, Subject subject, Semester currentSemester);

    boolean existsByEnrollStudent_UserIdAndEnrollSubject_SubjectIdAndSemester(Long userId, Long SubjectId, Semester semester);

    List<Enrollment> findAllByEnrollStudent_UserId(Long userId);
}
