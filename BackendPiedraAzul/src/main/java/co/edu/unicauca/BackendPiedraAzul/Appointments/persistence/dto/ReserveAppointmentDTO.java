package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class ReserveAppointmentDTO {

    private long idPatient;
    private long idDoctor;
    private IntervalDTO interval;
    private LocalDate appointmentDate;
}
