package org.pos.project.possystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pos.project.possystem.util.UserType;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {

    private Integer id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserType userType;
}
