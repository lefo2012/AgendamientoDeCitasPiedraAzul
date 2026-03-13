package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.MedicalHistory;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.MedicalHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class})
public interface MedicalHistoryMapper {

    MedicalHistory toDomain(MedicalHistoryEntity medicalHistoryEntity);
    MedicalHistoryEntity toEntity(MedicalHistory medicalHistory);
}
