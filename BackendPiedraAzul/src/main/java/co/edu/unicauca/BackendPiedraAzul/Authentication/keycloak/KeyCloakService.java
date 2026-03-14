package co.edu.unicauca.BackendPiedraAzul.Authentication.keycloak;

import co.edu.unicauca.BackendPiedraAzul.Authentication.dto.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import java.util.List;

@Service
@Slf4j
public class KeyCloakService implements IKeycloakService {

    /**
     * Metodo para listar todos los usuarios de Keycloak
     * @return List<UserRepresentation>
     */
    public List<UserRepresentation> findAllUsers(){
        return KeycloakProvider.getRealmResource()
                .users()
                .list();
    }

    /**
     * Metodo para buscar un usuario por su username
     * @return List<UserRepresentation>
     */
    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }

    /**
     * Metodo para crear un usuario en keycloak
     * @return String
     */
    public String createUser(UserRequest userDTO, String firstName, String lastName) {
        int status;
        UsersResource usersResource = KeycloakProvider.getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(firstName);
        userRepresentation.setLastName(lastName);
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        try (Response response = usersResource.create(userRepresentation)) {
            status = response.getStatus();

            if (status == 201) {
                String path = response.getLocation().getPath();
                String userId = path.substring(path.lastIndexOf("/") + 1);

                CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                credentialRepresentation.setTemporary(false);
                credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                credentialRepresentation.setValue(userDTO.getPassword());
                usersResource.get(userId).resetPassword(credentialRepresentation);
                RealmResource realmResource = KeycloakProvider.getRealmResource();

                List<RoleRepresentation> rolesRepresentation;

                if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {
                    rolesRepresentation = List.of(realmResource.roles().get("user").toRepresentation());
                } else {
                    rolesRepresentation = realmResource.roles()
                            .list()
                            .stream()
                            .filter(role -> userDTO.getRoles()
                                    .stream()
                                    .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                            .toList();
                    System.out.println(userDTO.getRoles().toString());
                }

                realmResource.users().get(userId).roles().realmLevel().add(rolesRepresentation);

                return "User created successfully!!";

            } else if (status == 409) {
                log.error("User exist already!");
                return "User exist already!";
            } else {
                log.error("Error creating user, please contact with the administrator.");
                return "Error creating user, please contact with the administrator.";
            }
        }
    }

    /**
     * Metodo para asignar roles del cliente a un usuario
     * @param userId ID del usuario en Keycloak
     * @param clientId ID del cliente en Keycloak
     * @param roleNames Lista de nombres de roles del cliente
     */
    public void assignClientRolesToUser(String userId, String clientId, List<String> roleNames) {
        try {
            RealmResource realmResource = KeycloakProvider.getRealmResource();

            // Obtener el cliente
            ClientResource clientResource = realmResource.clients().get(clientId);

            // Obtener los roles del cliente
            List<RoleRepresentation> clientRoles = clientResource.roles()
                    .list()
                    .stream()
                    .filter(role -> roleNames.stream()
                            .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                    .toList();

            // Asignar los roles del cliente al usuario
            if (!clientRoles.isEmpty()) {
                realmResource.users().get(userId).roles().clientLevel(clientId).add(clientRoles);
                log.info("Client roles assigned successfully to user: " + userId);
            } else {
                log.warn("No client roles found for: " + roleNames);
            }
        } catch (Exception e) {
            log.error("Error assigning client roles: ", e);
        }
    }

    /**
     * Metodo para crear un usuario en keycloak con roles del realm y del cliente
     * @param userDTO Datos del usuario
     * @param firstName Nombre del usuario
     * @param lastName Apellido del usuario
     * @param clientId ID del cliente para asignar roles del cliente
     * @return String mensaje de éxito o error
     */
    public String createUserWithClientRoles(UserRequest userDTO, String firstName, String lastName, String clientId) {
        String result = createUser(userDTO, firstName, lastName);

        if (result.equals("User created successfully!!")) {
            // Obtener el ID del usuario creado
            List<UserRepresentation> users = searchUserByUsername(userDTO.getEmail());
            if (!users.isEmpty()) {
                String userId = users.get(0).getId();

                // Asignar roles del cliente si se proporcionaron
                if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
                    assignClientRolesToUser(userId, clientId, userDTO.getRoles());
                    log.info("Client roles assigned to newly created user: " + userId);
                }
            }
        }

        return result;
    }

    /**
     * Metodo para borrar un usuario en keycloak
     */
    public void deleteUser(String userId){
        KeycloakProvider.getUserResource()
                .get(userId)
                .remove();
    }

}