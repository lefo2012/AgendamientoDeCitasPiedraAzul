package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto;

import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.DoctorDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.PatientDTO;

import java.time.LocalDate;

public class AppointmentDto {
    PatientDTO patient;
    DoctorDTO doctor;
    IntervalDTO interval;
    LocalDate appointmentDate;
}
