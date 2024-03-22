package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.exceptions.SemesterNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.UserNotFoundException;
import edu.agh.dean.classesverifierbe.model.Enrollment;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.repository.EnrollmentRepository;
import edu.agh.dean.classesverifierbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SemesterService semesterService;

    public List<Enrollment> getEnrolledSubjectsByUserId(Long userId) throws UserNotFoundException, SemesterNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id"));
        Semester currentSemester = semesterService.getCurrentSemester();
        return enrollmentRepository.findByEnrollStudentAndSemester(user, currentSemester);
    }

    public List<Enrollment> getEnrolledSubjectsByUserIndex(String index) throws UserNotFoundException, SemesterNotFoundException {
        User user = userRepository.findByIndexNumber(index)
                .orElseThrow(() -> new UserNotFoundException("index", index));
        Semester currentSemester = semesterService.getCurrentSemester();
        return enrollmentRepository.findByEnrollStudentAndSemester(user, currentSemester);
    }
}
