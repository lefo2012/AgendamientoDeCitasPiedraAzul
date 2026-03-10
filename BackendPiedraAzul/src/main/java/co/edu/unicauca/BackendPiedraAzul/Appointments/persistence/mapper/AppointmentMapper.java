package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.AppointmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class, PatientMapper.class, IntervalMapper.class})
public interface AppointmentMapper {

        AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);
        Appointment toDomain(AppointmentEntity appointmentEntity);
        AppointmentEntity toEntity(Appointment appointment);
}
