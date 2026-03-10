package Appointments.persistence.repository;

import Appointments.persistence.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepositoryJPA extends JpaRepository<PersonEntity,Long> {
}
