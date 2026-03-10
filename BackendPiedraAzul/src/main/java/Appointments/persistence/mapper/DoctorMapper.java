package Appointments.persistence.mapper;

import Appointments.domain.Doctor;
import Appointments.persistence.entities.DoctorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {AppointmentMapper.class, ScheduleMapper.class})
public interface DoctorMapper {

    Doctor toDomain(DoctorEntity doctorEntity);
    DoctorEntity toEntity(Doctor doctor);
}
