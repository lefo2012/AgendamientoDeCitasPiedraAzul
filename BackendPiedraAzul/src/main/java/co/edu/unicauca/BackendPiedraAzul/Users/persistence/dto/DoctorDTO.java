package co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.SpecialtyEnum;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.IntervalDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DoctorDTO {
    private Long id;
    private String documentType;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String phone;
    private boolean active;
    private UserDTO user;
    private List<SpecialtyEnum> specialties;
    private boolean canSchedule;
    private IntervalDTO appointmentInterval;
    private String gender;
}
