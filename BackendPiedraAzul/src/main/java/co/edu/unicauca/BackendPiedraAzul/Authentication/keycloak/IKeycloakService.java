package co.edu.unicauca.BackendPiedraAzul.Authentication.keycloak;


import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.UserDTO;
import jakarta.transaction.Transactional;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeycloakService {

    @Transactional
    List<UserRepresentation> findAllUsers();
    @Transactional
    List<UserRepresentation> searchUserByUsername(String username);
    @Transactional
    String createUser(UserDTO userDTO, String firstName, String lastName);
    @Transactional
    void deleteUser(String userId);
    @Transactional
    void assignClientRolesToUser(String userId, String clientId, List<String> roleNames);
    @Transactional
    String createUserWithClientRoles(UserDTO userDTO, String firstName, String lastName, String clientId);
}