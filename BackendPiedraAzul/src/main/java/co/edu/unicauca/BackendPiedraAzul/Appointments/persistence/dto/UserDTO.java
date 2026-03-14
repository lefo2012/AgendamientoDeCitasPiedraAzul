package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String email;
    private String password;
    private List<String> roles;
}
