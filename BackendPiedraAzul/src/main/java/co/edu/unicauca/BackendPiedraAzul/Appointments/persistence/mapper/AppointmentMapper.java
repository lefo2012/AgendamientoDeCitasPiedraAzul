package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.AppointmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {IntervalMapper.class})
public interface AppointmentMapper {

    @Mapping(target = "doctor.scheduledAppointments",ignore = true)
    @Mapping(target = "doctor.attendedAppointments",ignore = true)
    @Mapping(target = "patient.pastAppointments",ignore = true)
    @Mapping(target = "patient.pendingAppointments",ignore = true)
    Appointment toDomain(AppointmentEntity appointmentEntity) throws Exception;

    @Mapping(target = "doctor.scheduledAppointments",ignore = true)
    @Mapping(target = "doctor.attendedAppointments",ignore = true)
    @Mapping(target = "patient.pastAppointments",ignore = true)
    @Mapping(target = "patient.pendingAppointments",ignore = true)
    AppointmentEntity toEntity(Appointment appointment);

}