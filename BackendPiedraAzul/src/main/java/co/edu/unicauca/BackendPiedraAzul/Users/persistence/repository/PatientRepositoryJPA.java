package co.edu.unicauca.BackendPiedraAzul.Users.persistence.repository;

import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.PatientEntity;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepositoryJPA extends JpaRepository<PatientEntity,Long> {

    @Query("SELECT p FROM PatientEntity p WHERE p.identificationNumber = :identificationNumber")
    Optional<PatientEntity> findByIdentificationNumber(String identificationNumber) throws Exception;
}
