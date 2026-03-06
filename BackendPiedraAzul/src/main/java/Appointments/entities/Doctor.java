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
public class Doctor extends Person {

    @ElementCollection
    private List<SpecialtyEnum> specialties;
    @OneToMany
    private List<Appointment> scheduledAppointments;
    @OneToMany
    private List<Appointment> attendedAppointments;

    //Think about a value object to make date distribution easier
    @OneToOne(cascade = CascadeType.ALL)
    private Schedule schedule;

    public boolean addAppointmentToAttend(Appointment appointment) {
        if (scheduledAppointments == null) {
            scheduledAppointments = new ArrayList<>();
        }
        scheduledAppointments.add(appointment);
        return true;

    }

}
