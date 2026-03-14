package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.PatientDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.PatientEntity;
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