package co.edu.unicauca.BackendPiedraAzul.Authentication.controller;

import co.edu.unicauca.BackendPiedraAzul.Authentication.dto.UserRequest;
import co.edu.unicauca.BackendPiedraAzul.Authentication.dto.UserWithRolesDTO;
import co.edu.unicauca.BackendPiedraAzul.Authentication.keycloak.IKeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class KeycloakController {

    private final IKeycloakService keycloakService;
    private static final String CLIENT_ID = "piedraAzul-app";

    /**
     * Endpoint para crear un usuario con roles del cliente
     * POST /api/users/create
     * @param userRequest DTO con email, password y roles
     * @return ResponseEntity con mensaje de éxito o error
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody UserRequest userRequest) {
        try {
            String result = keycloakService.createUserWithClientRoles(
                    userRequest.getUserDTO(),
                    userRequest.getFirstName() != null ? userRequest.getFirstName() : "Usuario",
                    userRequest.getLastName() != null ? userRequest.getLastName() : "Nuevo",
                    CLIENT_ID
            );

            if (result.equals("User created successfully!!")) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of(
                                "status", "success",
                                "message", result,
                                "email", userRequest.getEmail()
                        ));
            } else if (result.equals("User exist already!")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of(
                                "status", "error",
                                "message", result
                        ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of(
                                "status", "error",
                                "message", result
                        ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al crear usuario: " + e.getMessage()
                    ));
        }
    }

    /**
     * Endpoint para listar todos los usuarios
     * GET /api/users/all
     * @return List de usuarios
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<UserRepresentation>> getAllUsers() {
        try {
            List<UserRepresentation> users = keycloakService.findAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para listar todos los usuarios con sus roles
     * GET /api/users/all-with-roles
     * @return List de usuarios con roles
     */
    @GetMapping("/all-with-roles")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<UserWithRolesDTO>> getAllUsersWithRoles() {
        try {
            List<UserWithRolesDTO> users = keycloakService.findAllUsersWithRoles();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para buscar un usuario por username
     * GET /api/users/search?username={username}
     * @param username nombre del usuario a buscar
     * @return List de usuarios encontrados
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<UserRepresentation>> searchUser(@RequestParam String username) {
        try {
            List<UserRepresentation> users = keycloakService.searchUserByUsername(username);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para asignar roles del cliente a un usuario
     * POST /api/users/{userId}/assign-client-roles
     * @param userId ID del usuario en Keycloak
     * @param roleRequest mapa con la lista de nombres de roles del cliente
     * @return ResponseEntity con mensaje de éxito o error
     */
    @PostMapping("/{userId}/assign-client-roles")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Map<String, String>> assignClientRoles(
            @PathVariable String userId,
            @RequestBody Map<String, List<String>> roleRequest) {
        try {
            List<String> roleNames = roleRequest.get("roles");
            if (roleNames == null || roleNames.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "status", "error",
                                "message", "Debe proporcionar al menos un rol"
                        ));
            }

            keycloakService.assignClientRolesToUser(userId, CLIENT_ID, roleNames);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Roles del cliente asignados exitosamente",
                    "userId", userId,
                    "rolesAssigned", String.join(", ", roleNames)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al asignar roles: " + e.getMessage()
                    ));
        }
    }

    /**
     * Endpoint para eliminar un usuario
     * DELETE /api/users/{userId}
     * @param userId ID del usuario a eliminar
     * @return ResponseEntity con mensaje de éxito o error
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String userId) {
        try {
            keycloakService.deleteUser(userId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Usuario eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al eliminar usuario: " + e.getMessage()
                    ));
        }
    }
}
