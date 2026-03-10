package Appointments.persistence.repository;

import Appointments.persistence.entities.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepositoryJPA extends JpaRepository<DoctorEntity,Long> {
}
