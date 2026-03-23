package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.repository;

import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepositoryJPA extends JpaRepository<ScheduleEntity,Long> {
}
