package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.exceptions.SubjectAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.SubjectNotFoundException;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagNotFoundException;
import edu.agh.dean.classesverifierbe.model.Subject;
import edu.agh.dean.classesverifierbe.model.SubjectTag;
import edu.agh.dean.classesverifierbe.repository.SubjectRepository;
import edu.agh.dean.classesverifierbe.repository.SubjectTagRepository;
import edu.agh.dean.classesverifierbe.specifications.SubjectSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectTagRepository subjectTagRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, SubjectTagRepository subjectTagRepository) {
        this.subjectRepository = subjectRepository;
        this.subjectTagRepository = subjectTagRepository;
    }

    public Subject createSubject(Subject subject) throws SubjectAlreadyExistsException {
        Subject foundSubject = subjectRepository.findByName(subject.getName());
        if(foundSubject != null){
            throw new SubjectAlreadyExistsException("name",subject.getName(),"subjects");
        }
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(Long subjectId,Subject subject) throws SubjectNotFoundException {
        Subject foundSubject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("subjectId"));
        foundSubject.setName(subject.getName());
        foundSubject.setDescription(subject.getDescription());
        return subjectRepository.save(foundSubject);
    }

    public Subject deleteSubject(Long subjectId) throws SubjectNotFoundException {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("subjectId"));

        subject.getSubjectTags().clear();
        subjectRepository.delete(subject);
        return subject;
    }

    public Page<Subject> getAllSubjects(String tags, String name, Pageable pageable) throws SubjectNotFoundException {
        Specification<Subject> spec = Specification
                .where(SubjectSpecifications.withTags(tags))
                .and(SubjectSpecifications.withNameLike(name));

        Page<Subject> subjects = subjectRepository.findAll(spec, pageable);

        if (subjects.isEmpty()) {
            throw new SubjectNotFoundException();
        }

        return subjects;
    }


    public Subject addTagToSubject(Long subjectId, Long tagId) throws SubjectNotFoundException, SubjectTagNotFoundException, SubjectTagAlreadyExistsException{
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("subjectId"));
        SubjectTag tag = subjectTagRepository.findById(tagId)
                .orElseThrow(() -> new SubjectTagNotFoundException("tagId"));

        if(subject.getSubjectTags().contains(tag)){
            throw new SubjectTagAlreadyExistsException("tag",tag.toString(),"subjectTags");
        }
        subject.getSubjectTags().add(tag);
        return subjectRepository.save(subject);
    }



    public Subject removeTagFromSubject(Long subjectId, Long tagId) throws SubjectNotFoundException, SubjectTagNotFoundException{
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("subjectId"));
        SubjectTag tag = subjectTagRepository.findById(tagId)
                .orElseThrow(() -> new SubjectTagNotFoundException("tagId"));

        if(!subject.getSubjectTags().contains(tag)){
            throw new SubjectTagNotFoundException("tag",tag.toString(),"subjectTags");
        }
        subject.getSubjectTags().remove(tag);
        return subjectRepository.save(subject);
    }

    public Subject getSubjectById(Long subjectId) throws SubjectNotFoundException {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("subjectId"));
    }

}
