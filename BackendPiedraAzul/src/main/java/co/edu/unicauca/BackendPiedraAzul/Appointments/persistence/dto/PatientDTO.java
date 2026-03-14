package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientDTO {
    protected String documentType;
    protected String identificationNumber;
    protected String firstName;
    protected String lastName;
    protected String birthDate;
    protected String phone;
    protected boolean active;
    protected UserDTO user;
}
