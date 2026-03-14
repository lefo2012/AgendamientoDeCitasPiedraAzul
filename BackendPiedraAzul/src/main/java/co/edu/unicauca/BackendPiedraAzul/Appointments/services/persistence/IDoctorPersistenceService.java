package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Doctor;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IDoctorPersistenceService {
    @Transactional
    Doctor save(Doctor doctor) throws Exception;

    @Transactional
    List<Doctor> findAll () throws Exception;
}
