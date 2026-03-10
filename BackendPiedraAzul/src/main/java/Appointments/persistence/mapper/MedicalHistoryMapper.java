package Appointments.persistence.mapper;

import Appointments.domain.MedicalHistory;
import Appointments.persistence.entities.MedicalHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class})
public interface MedicalHistoryMapper {

    MedicalHistory toDomain(MedicalHistoryEntity medicalHistoryEntity);
    MedicalHistoryEntity toEntity(MedicalHistory medicalHistory);
}
