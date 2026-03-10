package Appointments.persistence.repository;

import Appointments.persistence.entities.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepositoryJPA extends JpaRepository<PatientEntity,Long> {
}
