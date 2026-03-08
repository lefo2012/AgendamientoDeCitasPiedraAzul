package Appointments.persistence.mapper;

import Appointments.domain.Doctor;
import Appointments.persistence.entities.DoctorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AppointmentMapper.class, ScheduleMapper.class})
public interface DoctorMapper extends PersonMapper{
    Doctor toDomain(DoctorEntity doctorEntity);
    DoctorEntity toEntity(Doctor doctor);
}
