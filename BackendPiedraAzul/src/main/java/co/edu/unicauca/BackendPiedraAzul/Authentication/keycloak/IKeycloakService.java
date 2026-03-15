package Authentication.keycloak;

import Authentication.dto.UserRequest;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeycloakService {

    List<UserRepresentation> findAllUsers();
    List<UserRepresentation> searchUserByUsername(String username);
    String createUser(UserDTO userDTO, String firstName, String lastName);
    void deleteUser(String userId);
    void assignClientRolesToUser(String userId, String clientId, List<String> roleNames);
    String createUserWithClientRoles(UserDTO userDTO, String firstName, String lastName, String clientId);
}