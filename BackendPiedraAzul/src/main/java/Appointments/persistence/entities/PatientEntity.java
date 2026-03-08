package Appointments.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PatientEntity extends PersonEntity {

    @OneToMany
    private List<AppointmentEntity> pendingAppointments;
    @OneToMany
    private List<AppointmentEntity> pastAppointments;
    @OneToOne
    private MedicalHistoryEntity medicalHistory;

    private int appointmentCount;

}
