package Appointments.persistence.repository;

import Appointments.persistence.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepositoryJPA extends JpaRepository<PersonEntity,Long> {
}
