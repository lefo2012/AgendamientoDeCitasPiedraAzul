package Appointments.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private Doctor doctor;
    @ManyToOne
    private Patient patient;
    private Date appointmentDate;
    @Enumerated(EnumType.STRING)
    private AppointmentStatusEnum appointmentStatus;

    /*
     * Function that creates an appointment within the scope of the entities
     * */
    Appointment scheduleAppointment(Doctor doctor, Date appointmentDate, Patient patient) {

        Appointment appointment = new Appointment(doctor, appointmentDate, patient);

        doctor.addAppointmentToAttend(appointment);

        patient.addPendingAppointment(appointment);

        return appointment;
    }

    private Appointment(Doctor doctor, Date appointmentDate, Patient patient) {
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.patient = patient;
        this.appointmentStatus = AppointmentStatusEnum.EN_PROCESO;
    }

}
