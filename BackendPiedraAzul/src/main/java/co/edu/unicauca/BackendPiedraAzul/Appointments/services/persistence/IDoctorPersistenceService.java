package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.DoctorDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IDoctorPersistenceService {
    @Transactional
    Doctor save(Doctor doctor) throws Exception;

    @Transactional
    Doctor save(DoctorDTO doctorDTO) throws Exception;

    @Transactional
    List<Doctor> findAll () throws Exception;
}
