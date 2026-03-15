package Authentication.dto;

import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserRequest {

    @Getter
    @Setter
    private String email;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private String firstName;
    @Getter @Setter
    private String lastName;
    @Getter @Setter
    private List<String> roles;

    @Getter @Setter
    private UserDTO userDTO;

}