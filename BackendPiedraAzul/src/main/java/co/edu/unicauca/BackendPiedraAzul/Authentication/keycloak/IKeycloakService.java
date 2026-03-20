package co.edu.unicauca.BackendPiedraAzul.Authentication.keycloak;

import co.edu.unicauca.BackendPiedraAzul.Authentication.dto.UserRequest;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeycloakService {

    List<UserRepresentation> findAllUsers();
    List<UserRepresentation> searchUserByUsername(String username);
    String createUser(UserRequest userDTO, String firstName, String lastName);
    void deleteUser(String userId);
    void assignClientRolesToUser(String userId, String clientId, List<String> roleNames);
    String createUserWithClientRoles(UserRequest userDTO, String firstName, String lastName, String clientId);
}