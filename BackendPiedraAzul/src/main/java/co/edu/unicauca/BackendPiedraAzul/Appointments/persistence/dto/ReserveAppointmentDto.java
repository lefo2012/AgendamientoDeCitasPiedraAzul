package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto;

import lombok.Getter;

import java.time.LocalDate;
@Getter
public class ReserveAppointmentDto {

    private long idPatient;
    private long idDoctor;
    private IntervalDTO interval;
    private LocalDate appointmentDate;
}
