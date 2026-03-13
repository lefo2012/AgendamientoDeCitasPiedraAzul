package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.DoctorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring",
        uses = {ScheduleMapper.class,AppointmentMapper.class}
)
public interface DoctorMapper {


    DoctorEntity toEntity(Doctor doctor);

    Doctor toDomain(DoctorEntity doctorEntity);


}