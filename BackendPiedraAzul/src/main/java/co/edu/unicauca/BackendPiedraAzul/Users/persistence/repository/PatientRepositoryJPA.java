package co.edu.unicauca.BackendPiedraAzul.Users.persistence.repository;

import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepositoryJPA extends JpaRepository<PatientEntity,Long> {
}
