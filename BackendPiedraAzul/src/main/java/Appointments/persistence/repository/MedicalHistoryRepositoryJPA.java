package Appointments.persistence.repository;

import Appointments.persistence.entities.MedicalHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalHistoryRepositoryJPA extends JpaRepository<MedicalHistoryEntity,Long> {
}
