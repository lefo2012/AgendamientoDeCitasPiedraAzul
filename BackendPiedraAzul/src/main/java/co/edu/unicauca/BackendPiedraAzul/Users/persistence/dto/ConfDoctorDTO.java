package co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConfDoctorDTO {
    private Long id;
    private String identificationNumber;
    private String firstName;
    private String lastName;
}
