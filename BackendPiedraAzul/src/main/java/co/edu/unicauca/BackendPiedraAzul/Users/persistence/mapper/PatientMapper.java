package co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.PatientDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.PatientEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.AppointmentMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.MedicalHistoryMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {MedicalHistoryMapper.class, AppointmentMapper.class, UserMapper.class}
)
public interface PatientMapper {
    PatientEntity toEntity(Patient patient);
    Patient toDomain(PatientEntity patientEntity);

    @Mapping(source = "birthDate", target = "birthDate", dateFormat = "yyyy-MM-dd")
    Patient dtoToDomain(PatientDTO patientDto);
}