package Appointments.persistence.mapper;

import Appointments.domain.Patient;
import Appointments.persistence.entities.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {AppointmentMapper.class, MedicalHistoryMapper.class})
public interface PatientMapper {


    Patient toDomain(PatientEntity patientEntity);
    PatientEntity toEntity(Patient patient);
}