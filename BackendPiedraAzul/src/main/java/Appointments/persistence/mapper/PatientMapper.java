package Appointments.persistence.mapper;

import Appointments.domain.Patient;
import Appointments.persistence.entities.PatientEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AppointmentMapper.class, MedicalHistoryMapper.class})
public interface PatientMapper extends PersonMapper{
    Patient toDomain(PatientEntity patientEntity);
    PatientEntity toEntity(Patient patient);
}
