package edu.agh.dean.classesverifierbe.RO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.agh.dean.classesverifierbe.model.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectTagRO {

    private int subjectTagId;

    private String name;

    private String description;

}
