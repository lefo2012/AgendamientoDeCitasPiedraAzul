package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto;

import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.DoctorDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.PatientDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentDto {
    PatientDTO patient;
    DoctorDTO doctor;
    IntervalDTO interval;
    LocalDate appointmentDate;
}
