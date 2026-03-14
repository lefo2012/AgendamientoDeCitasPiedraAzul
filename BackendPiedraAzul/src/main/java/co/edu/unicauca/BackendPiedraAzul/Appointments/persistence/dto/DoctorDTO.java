package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.SpecialtyEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DoctorDTO {
    protected String documentType;
    protected String identificationNumber;
    protected String firstName;
    protected String lastName;
    protected String birthDate;
    protected String phone;
    protected boolean active;
    protected UserDTO user;
    protected List<SpecialtyEnum> specialties;
}
