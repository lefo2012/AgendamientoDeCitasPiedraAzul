package co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.SpecialtyEnum;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.AppointmentEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.ScheduleEntity;
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

    @ElementCollection(targetClass = SpecialtyEnum.class)
    @Enumerated(EnumType.STRING)
    private List<SpecialtyEnum> specialties;
    @OneToMany(cascade = CascadeType.ALL)
    private List<AppointmentEntity> scheduledAppointments;
    @OneToMany(cascade = CascadeType.ALL)
    private List<AppointmentEntity> attendedAppointments;

    //Think about a value object to make date distribution easier
    @OneToOne(cascade = CascadeType.ALL)
    private ScheduleEntity schedule;

}
