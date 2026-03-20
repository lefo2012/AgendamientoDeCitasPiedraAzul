package co.edu.unicauca.BackendPiedraAzul.Appointments.entities;

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
public class Patient extends Person {

    @OneToMany
    private List<Appointment> pendingAppointments;
    @OneToMany
    private List<Appointment> pastAppointments;
    @OneToOne
    private MedicalHistory medicalHistory;
    private int appointmentCount;

    public boolean addPendingAppointment(Appointment appointment) {

        if (pendingAppointments == null) {
            pendingAppointments = new ArrayList<>();
        }
        pendingAppointments.add(appointment);
        appointmentCount++;

        return true;
    }

}
