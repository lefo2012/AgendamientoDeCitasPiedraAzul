package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities;

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

    @OneToMany(cascade = CascadeType.ALL)
    private List<AppointmentEntity> pendingAppointments;
    @OneToMany(cascade = CascadeType.ALL)
    private List<AppointmentEntity> pastAppointments;
    @OneToOne(cascade = CascadeType.ALL)
    private MedicalHistoryEntity medicalHistory;

    private int appointmentCount;

}
