package co.edu.unicauca.BackendPiedraAzul.Authentication.services;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IDoctorPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IPatientPersistenceService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService{
    private final IPatientPersistenceService  patientPersistenceService;

    private final IDoctorPersistenceService doctorPersistenceService;

    public AuthService(IPatientPersistenceService patientPersistenceService, IDoctorPersistenceService doctorPersistenceService) {
        this.doctorPersistenceService = doctorPersistenceService;
        this.patientPersistenceService = patientPersistenceService;
    }

    @Override
    public Patient getPatientByToken(Authentication authentication) throws Exception{
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        Jwt jwt = jwtToken.getToken();
        String keycloakId = jwt.getSubject();
        return patientPersistenceService.findByKeycloakId(keycloakId);
    }

    @Override
    public Doctor getDoctorByToken(Authentication authentication) throws Exception{
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        Jwt jwt = jwtToken.getToken();
        String keycloakId = jwt.getSubject();
        return doctorPersistenceService.findByKeycloakId(keycloakId);
    }
}
