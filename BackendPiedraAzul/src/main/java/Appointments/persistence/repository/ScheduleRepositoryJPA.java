package Appointments.persistence.repository;

import Appointments.persistence.entities.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepositoryJPA extends JpaRepository<ScheduleEntity,Long> {
}
