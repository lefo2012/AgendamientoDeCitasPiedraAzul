package Appointments.persistence.mapper;

import org.mapstruct.Mapper;
import Appointments.domain.Person;
import Appointments.persistence.entities.PersonEntity;

@Mapper (componentModel = "spring", uses = {UserMapper.class, DoctorMapper.class, PatientMapper.class})
public interface PersonMapper {
    Person toDomain(PersonEntity personEntity);
    PersonEntity toEntity(Person person);
}
