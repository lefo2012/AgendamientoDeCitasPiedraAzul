package co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientDTO {
    private Long id;
    private String documentType;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String phone;
    private boolean active;
    private UserDTO user;
    private String gender;
}
