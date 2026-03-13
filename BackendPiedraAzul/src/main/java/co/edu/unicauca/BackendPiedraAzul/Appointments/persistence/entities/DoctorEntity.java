package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.SpecialtyEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class DoctorEntity extends PersonEntity {

    @ElementCollection
    private List<SpecialtyEnum> specialties;
//    @OneToMany
//    private List<AppointmentEntity> scheduledAppointments;
//    @OneToMany
//    private List<AppointmentEntity> attendedAppointments;

    //Think about a value object to make date distribution easier
    @OneToOne(cascade = CascadeType.ALL)
    private ScheduleEntity schedule;

}
