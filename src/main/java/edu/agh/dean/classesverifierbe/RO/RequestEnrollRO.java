package edu.agh.dean.classesverifierbe.RO;

import edu.agh.dean.classesverifierbe.dto.SubjectDTO;
import edu.agh.dean.classesverifierbe.dto.UserDTO;
import edu.agh.dean.classesverifierbe.model.enums.RequestEnrollStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestEnrollRO {
    private Long requestEnrollId;
    private RequestEnrollStatus requestStatus;
    private UserDTO user;
    private SubjectDTO subject;
}

