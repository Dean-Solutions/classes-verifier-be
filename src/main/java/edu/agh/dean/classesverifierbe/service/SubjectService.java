package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.RO.UserRO;
import edu.agh.dean.classesverifierbe.dto.SubjectDTO;
import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.exceptions.*;
import edu.agh.dean.classesverifierbe.model.Semester;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.SubjectTag;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.repository.SubjectRepository;
import edu.agh.dean.classesverifierbe.repository.SubjectTagRepository;
import edu.agh.dean.classesverifierbe.specifications.SubjectSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectTagRepository subjectTagRepository;

    private final SemesterService semesterService;

    public Subject createSubject(Subject subject, Set<String> tagNames) throws SubjectAlreadyExistsException {
        if(subjectRepository.findByName(subject.getName()) != null){
            throw new SubjectAlreadyExistsException("name", subject.getName(), "subjects");
        }
        Set<SubjectTag> tags = getTags(tagNames);
        subject.setSubjectTags(tags);
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(Long subjectId, Subject subject, Set<String> tagNames) throws SubjectNotFoundException {
        Subject foundSubject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("subjectId"));

        foundSubject.setName(subject.getName());
        foundSubject.setDescription(subject.getDescription());
        foundSubject.setSemester(subject.getSemester());

        Set<SubjectTag> newTags = getTags(tagNames);

        foundSubject.setSubjectTags(newTags);

        return subjectRepository.save(foundSubject);
    }

    private Set<SubjectTag> getTags(Set<String> tagNames) {
        return tagNames.stream()
                .map(name -> subjectTagRepository.findByName(name)
                        .orElseGet(() -> {
                            SubjectTag newTag = SubjectTag.builder()
                                    .name(name)
                                    .description("")
                                    .build();
                            return subjectTagRepository.save(newTag);
                        }))
                .collect(Collectors.toSet());
    }


    public Subject deleteSubject(Long subjectId) throws SubjectNotFoundException {
        Subject subject = getSubjectById(subjectId);

        subject.getSubjectTags().clear();
        subjectRepository.delete(subject);
        return subject;
    }

    public Page<Subject> getAllSubjects(String tags, String name, Integer semester,Pageable pageable) {
        Specification<Subject> spec = Specification
                .where(SubjectSpecifications.withTags(tags))
                .and(SubjectSpecifications.withNameLike(name))
                .and(SubjectSpecifications.withSemester(semester));

        Page<Subject> subjects = subjectRepository.findAll(spec, pageable);

        return subjects;
    }

    public Subject getSubjectById(Long subjectId) throws SubjectNotFoundException {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("subjectId"));
    }

    public List<UserRO> getUsersEnrolledInSubjectForSemester(Long subjectId, Long semesterId) throws SubjectNotFoundException, SemesterNotFoundException {
        if(semesterId == null){
            semesterId = semesterService.getCurrentSemester().getSemesterId();
        }
        final Long  properSemesterId = semesterId;

        Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
        if (subjectOpt.isEmpty()) {
            throw new SubjectNotFoundException();
        }
        Subject subject = subjectOpt.get();

        return subject.getEnrollments().stream()
                .filter(enrollment -> enrollment.getSemester().getSemesterId().equals(properSemesterId))
                .map(enrollment -> {
                    User user = enrollment.getEnrollStudent();
                    UserRO userRO = UserRO.builder()
                            .userId(user.getUserId())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .email(user.getEmail())
                            .indexNumber(user.getIndexNumber())
                            .eduPath(user.getEduPath())
                            .role(user.getRole())
                            .build();
                    return userRO;
                })
                .collect(Collectors.toList());
    }


    public List<Subject> getAllSubjectsBySemester(Integer semester) {
        return subjectRepository.findBySemester(semester);
    }


}
