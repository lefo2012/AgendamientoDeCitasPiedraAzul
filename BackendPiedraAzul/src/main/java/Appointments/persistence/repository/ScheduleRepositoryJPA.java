package Appointments.persistence.repository;

import Appointments.persistence.entities.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepositoryJPA extends JpaRepository<ScheduleEntity,Long> {
}
