package Appointments.persistence.mapper;

import Appointments.domain.Appointment;
import Appointments.persistence.entities.AppointmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class, PatientMapper.class, IntervalMapper.class})
public interface AppointmentMapper {
        Appointment toDomain(AppointmentEntity appointmentEntity);
        AppointmentEntity toEntity(Appointment appointment);
}
