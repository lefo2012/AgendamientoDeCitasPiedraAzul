package Appointments.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    public boolean addPendingAppointment(AppointmentEntity appointment) {

        if (pendingAppointments == null) {
            pendingAppointments = new ArrayList<>();
        }
        pendingAppointments.add(appointment);
        appointmentCount++;

        return true;
    }

}
