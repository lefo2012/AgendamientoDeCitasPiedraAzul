package co.edu.unicauca.BackendPiedraAzul.Users.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Authentication.keycloak.IKeycloakService;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.DoctorDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.PatientDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper.DoctorMapper;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper.PatientMapper;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IDoctorPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IPatientPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class DoctorService implements IDoctorService {
    private final IKeycloakService keycloakService;

    private final IDoctorPersistenceService doctorService;

    private final DoctorMapper doctorMapper;

    public DoctorService(IKeycloakService keycloakService,
                         IDoctorPersistenceService doctorService, DoctorMapper doctorMapper) {
        this.keycloakService = keycloakService;
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
    }

    public Doctor register(DoctorDTO doctorDto, String client_id) throws Exception {

        // 1️⃣ crear usuario en keycloak
        String keycloakId =
                keycloakService.createUserWithClientRoles(doctorDto.getUser(), doctorDto.getFirstName(), doctorDto.getLastName(), client_id);

        // 2️⃣ crear dominio
        Doctor doctor = doctorMapper.dtoToDomain(doctorDto);

        return doctorService.save(doctor);

    }
}
