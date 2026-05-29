package co.edu.unicauca.BackendPiedraAzul.Users.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Authentication.keycloak.IKeycloakService;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Person;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.PatientDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.UserDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper.PatientMapper;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IPatientPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IPersonPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IUserPersistenceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService implements IPatientService {

    private final IKeycloakService keycloakService;

    private final IPatientPersistenceService patientService;

    private final PatientMapper patientMapper;

    public PatientService(IKeycloakService keycloakService,
                          IPatientPersistenceService patientService, PatientMapper patientMapper) {
        this.keycloakService = keycloakService;
        this.patientService = patientService;
        this.patientMapper = patientMapper;
    }

    public Patient register(PatientDTO patientDto, String client_id) throws Exception {

        // 1️⃣ crear usuario en keycloak
        String keycloakId =
                keycloakService.createUserWithClientRoles(patientDto.getUser(), patientDto.getFirstName(), patientDto.getLastName(), client_id);

        // 2️⃣ crear dominio
        Patient patient = patientMapper.dtoToDomain(patientDto);
        patient.getUser().setKeycloakId(keycloakId);

        return patientService.save(patient);

    }

    @Override
     public List<Appointment> getPendingAppointments(Long patientId) throws Exception{
        Patient patient = patientService.findById(patientId);
        return patient.getPendingAppointments();
    }

}
