package Appointments.persistence.entities;

import Appointments.domain.AppointmentStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

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


}
