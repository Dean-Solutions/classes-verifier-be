package edu.agh.dean.classesverifierbe.service;

import edu.agh.dean.classesverifierbe.RO.SubjectTagRO;
import edu.agh.dean.classesverifierbe.dto.SubjectTagDTO;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagAlreadyExistsException;
import edu.agh.dean.classesverifierbe.exceptions.SubjectTagNotFoundException;
import edu.agh.dean.classesverifierbe.model.SubjectTag;
import edu.agh.dean.classesverifierbe.repository.SubjectTagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
@Service
public class SubjectTagService {

    private final SubjectTagRepository subjectTagRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SubjectTagService(SubjectTagRepository subjectTagRepository, ModelMapper modelMapper) {
        this.subjectTagRepository = subjectTagRepository;
        this.modelMapper = modelMapper;
    }

    public SubjectTagRO createTag(SubjectTagDTO tagDto) throws SubjectTagAlreadyExistsException {
        Optional<SubjectTag> existingTag = subjectTagRepository.findByName(tagDto.getName());
        if (existingTag.isPresent()) {
            throw new SubjectTagAlreadyExistsException();
        }

        SubjectTag tag = modelMapper.map(tagDto, SubjectTag.class);
        SubjectTag savedTag = subjectTagRepository.save(tag);
        return modelMapper.map(savedTag, SubjectTagRO.class);
    }

    public List<SubjectTagRO> getAllTags() {
        List<SubjectTag> tags =(List<SubjectTag>) subjectTagRepository.findAll();
        return tags.stream()
                .map(tag -> modelMapper.map(tag, SubjectTagRO.class))
                .collect(Collectors.toList());
    }

    public SubjectTagRO getTag(Long tagId) throws SubjectTagNotFoundException {
        SubjectTag tag = subjectTagRepository.findById(tagId)
                .orElseThrow(() -> new SubjectTagNotFoundException("TagId",tagId.toString()));
        return modelMapper.map(tag, SubjectTagRO.class);
    }

    public SubjectTagRO updateTag(Long tagId, SubjectTagDTO tagDto) throws SubjectTagNotFoundException {
        SubjectTag existingTag = subjectTagRepository.findById(tagId)
                .orElseThrow(() -> new SubjectTagNotFoundException("TagId",tagId.toString()));

        existingTag.setName(tagDto.getName());
        existingTag.setDescription(tagDto.getDescription());
        SubjectTag updatedTag = subjectTagRepository.save(existingTag);
        return modelMapper.map(updatedTag, SubjectTagRO.class);
    }

    public SubjectTagRO deleteTag(Long tagId) throws SubjectTagNotFoundException {
        SubjectTag tag = subjectTagRepository.findById(tagId)
                .orElseThrow(() -> new SubjectTagNotFoundException("TagId",tagId.toString()));
        subjectTagRepository.delete(tag);
        return modelMapper.map(tag, SubjectTagRO.class); // Zwracanie obiektu po usunięciu dla celów informacyjnych
    }
}
