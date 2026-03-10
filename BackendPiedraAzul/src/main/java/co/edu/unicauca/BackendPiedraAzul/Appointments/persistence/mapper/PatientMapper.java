package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.PatientEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AppointmentMapper.class, MedicalHistoryMapper.class})
public interface PatientMapper {


    Patient toDomain(PatientEntity patientEntity);
    PatientEntity toEntity(Patient patient);
}