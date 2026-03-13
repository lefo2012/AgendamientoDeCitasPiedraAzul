package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.DoctorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ScheduleMapper.class})
public interface DoctorMapper {

    Doctor toDomain(DoctorEntity doctorEntity);
    DoctorEntity toEntity(Doctor doctor);
}
