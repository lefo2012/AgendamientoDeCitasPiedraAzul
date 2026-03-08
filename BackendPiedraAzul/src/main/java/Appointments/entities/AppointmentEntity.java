package Appointments.entities;

import Appointments.domain.AppointmentStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Date appointmentDate;
    @Enumerated(EnumType.STRING)
    private AppointmentStatusEnum appointmentStatus;

    /*
     * Function that creates an appointment within the scope of the entities
     * */
    AppointmentEntity scheduleAppointment(DoctorEntity doctor, Date appointmentDate, PatientEntity patient) {

        AppointmentEntity appointment = new AppointmentEntity(doctor, appointmentDate, patient);

        doctor.addAppointmentToAttend(appointment);

        patient.addPendingAppointment(appointment);

        return appointment;
    }

    private AppointmentEntity(DoctorEntity doctor, Date appointmentDate, PatientEntity patient) {
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.patient = patient;
        this.appointmentStatus = AppointmentStatusEnum.EN_PROCESO;
    }

}
