package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.AppointmentStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    private DoctorEntity doctor;
    @ManyToOne
    private PatientEntity patient;
    private LocalDate appointmentDate;
    @Enumerated(EnumType.STRING)
    private AppointmentStatusEnum appointmentStatus;
    private IntervalEntity interval;


}
