package Appointments.persistence.repository;
import Appointments.persistence.entities.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepositoryJPA extends  JpaRepository<AppointmentEntity,Long> {
}
