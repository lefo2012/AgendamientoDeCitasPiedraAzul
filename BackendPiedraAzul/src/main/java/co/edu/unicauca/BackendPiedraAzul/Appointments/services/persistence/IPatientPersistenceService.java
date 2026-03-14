package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.PatientDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IPatientPersistenceService {

    @Transactional
    Patient save(Patient patient) throws Exception;

    @Transactional
    Patient save(PatientDTO patient) throws Exception;

    @Transactional
    List<Patient> findAll() throws Exception;
}
