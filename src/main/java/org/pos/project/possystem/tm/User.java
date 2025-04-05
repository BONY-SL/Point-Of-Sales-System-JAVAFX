package org.pos.project.possystem.tm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pos.project.possystem.util.UserType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    private Integer id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserType userType;

}
