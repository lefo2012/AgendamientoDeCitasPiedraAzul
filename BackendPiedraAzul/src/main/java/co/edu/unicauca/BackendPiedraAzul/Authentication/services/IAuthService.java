package co.edu.unicauca.BackendPiedraAzul.Authentication.services;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import org.springframework.security.core.Authentication;

public interface IAuthService {
    Patient getPatientByToken(Authentication authentication) throws Exception;

}
