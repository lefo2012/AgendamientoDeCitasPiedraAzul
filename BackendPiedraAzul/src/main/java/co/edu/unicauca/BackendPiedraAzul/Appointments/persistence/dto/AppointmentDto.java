package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
public class AppointmentDto {

    private long idPatient;
    private long idDoctor;
    private IntervalDTO interval;
    private LocalDate appointmentDate;
}
