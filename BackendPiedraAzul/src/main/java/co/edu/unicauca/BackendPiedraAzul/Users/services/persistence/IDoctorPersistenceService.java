package co.edu.unicauca.BackendPiedraAzul.Users.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.DoctorDTO;
import jakarta.transaction.Transactional;

import java.util.List;

//TOQUETIE ESTO
public interface IDoctorPersistenceService {
    @Transactional
    Doctor save(Doctor doctor) throws Exception;

    @Transactional
    Doctor save(DoctorDTO doctorDTO) throws Exception;

    @Transactional
    List<Doctor> findAll () throws Exception;
    //TOQUETIE ESTO
    @Transactional
    Doctor findById (Long id) throws Exception;
}
