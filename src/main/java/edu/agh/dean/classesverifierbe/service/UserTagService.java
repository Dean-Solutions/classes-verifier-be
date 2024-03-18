package edu.agh.dean.classesverifierbe.service;


import edu.agh.dean.classesverifierbe.dto.UserTagDTO;
import edu.agh.dean.classesverifierbe.model.User;
import edu.agh.dean.classesverifierbe.model.UserTag;
import edu.agh.dean.classesverifierbe.repository.UserTagRepository;
import edu.agh.dean.classesverifierbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTagService {
    @Autowired
    private UserTagRepository userTagRepository;

    public UserTag createTag(UserTagDTO tagDTO) {
        if (userTagRepository.findByName(tagDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Tag with this name already exists");
        }
        UserTag newTag = new UserTag();
        newTag.setName(tagDTO.getName());
        newTag.setDescription(tagDTO.getDescription());
        return userTagRepository.save(newTag);
    }


    public Iterable<UserTag> getAllTags() {
        return userTagRepository.findAll();
    }

    public boolean tagExistsByName(String tagName) {
        return userTagRepository.findByName(tagName).isPresent();
    }

}
