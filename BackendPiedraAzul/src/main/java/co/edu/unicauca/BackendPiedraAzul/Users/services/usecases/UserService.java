package co.edu.unicauca.BackendPiedraAzul.Users.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Authentication.keycloak.IKeycloakService;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Person;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.UserDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper.UserMapper;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IPersonPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IUserPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {


    private final IKeycloakService keycloakService;

    private final IUserPersistenceService userRepository;

    private final UserMapper userMapper;

    public UserService(IKeycloakService keycloakService,
                               IUserPersistenceService userRepository, UserMapper userMapper) {
        this.keycloakService = keycloakService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User register(UserDTO userDTO, String firstName, String lastName, String client_id) {

        // 1️⃣ crear usuario en keycloak
        String keycloakId =
                keycloakService.createUserWithClientRoles(userDTO, firstName, lastName, client_id);

        // 2️⃣ crear dominio
        User user = userMapper.dtoToDomain(userDTO);

        return userRepository.save(user);

    }

}
