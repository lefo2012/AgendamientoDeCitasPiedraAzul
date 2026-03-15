package co.edu.unicauca.BackendPiedraAzul.Users.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.DoctorDTO;

public interface IDoctorService {
    Doctor register(DoctorDTO doctorDto, String client_id) throws Exception;
}
