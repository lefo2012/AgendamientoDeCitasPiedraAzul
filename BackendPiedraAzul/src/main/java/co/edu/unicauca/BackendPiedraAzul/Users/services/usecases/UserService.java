package co.edu.unicauca.BackendPiedraAzul.Users.services.usecases;

import Authentication.keycloak.IKeycloakService;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Person;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.UserDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IPersonPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IUserPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService implements IUserService {


    private final IKeycloakService keycloakService;

    private final IUserPersistenceService userRepository;

    private final IPersonPersistenceService personRepository;

    public UserService(IKeycloakService keycloakService,
                               IUserPersistenceService userRepository, IPersonPersistenceService personRepository) {
        this.keycloakService = keycloakService;
        this.userRepository = userRepository;
        this.personRepository = personRepository;
    }

    public void register(HIJODTO userDTO, String firstName, String lastName) {

        // 1️⃣ crear usuario en keycloak
        String keycloakId =
                keycloakService.createUser(userDTO, firstName, lastName);

        // 2️⃣ crear dominio
        User user = new User();
        user.setKeycloakId(keycloakId);
        user.setEmail(userDTO.getEmail());

    }

}
