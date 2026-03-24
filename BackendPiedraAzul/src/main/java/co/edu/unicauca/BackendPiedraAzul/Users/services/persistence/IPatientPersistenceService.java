package co.edu.unicauca.BackendPiedraAzul.Users.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.PatientDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IPatientPersistenceService {

    @Transactional
    Patient save(Patient patient) throws Exception;

    @Transactional
    Patient save(PatientDTO patient) throws Exception;

    @Transactional
    List<Patient> findAll() throws Exception;

    @Transactional
    Patient findById(Long id) throws Exception;

    @Transactional
    Patient findByIdentificationNumber(String identificationNumber) throws Exception;

    @Transactional
    Patient findByKeycloakId(String keycloakId) throws Exception;
}
