package edu.agh.dean.classesverifierbe.RO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRO {
    private Long subjectId;
    private String name;
    private String description;
    private Integer semester;
    private Set<SubjectTagRO> subjectTags;
}
