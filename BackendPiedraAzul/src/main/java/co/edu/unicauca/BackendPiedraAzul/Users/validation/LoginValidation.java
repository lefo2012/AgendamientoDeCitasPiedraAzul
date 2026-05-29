package co.edu.unicauca.BackendPiedraAzul.Users.validation;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IUserPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.usecases.IUserService;
import jakarta.transaction.Transactional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;



@Component
public class LoginValidation {

    @Value("${keycloak.token-url}")
    private String keycloakTokenUrl;

    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.client-secret}")
    private String keycloakClientSecret;

    @Value("${keycloak.test.username}")
    private String keycloakTestUsername;

    @Value("${keycloak.test.password}")
    private String keycloakTestPassword;

    @Value("${backend.test.login-url}")
    private String backendTestLoginUrl;

    public void testLoginFlow() {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", keycloakClientId);
        body.add("grant_type", "password");
        body.add("username", keycloakTestUsername);
        body.add("password", keycloakTestPassword);
        if (keycloakClientSecret != null && !keycloakClientSecret.isBlank()) {
            body.add("client_secret", keycloakClientSecret);
        }

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);
        ResponseEntity<Map> response;
        try {
            response = restTemplate.postForEntity(keycloakTokenUrl, request, Map.class);

        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Credenciales incorrectas. Login rechazado.");
            return;
        } catch (Exception e) {

            System.out.println("Error al comunicarse con Keycloak.");
            return;
        }

        String token = (String) response.getBody().get("access_token");

        if (token == null) {
            System.out.println("Keycloak no devolvió token. Login falló.");
            return;
        }

        // ahora probamos el endpoint protegido
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(authHeaders);

        try {

            ResponseEntity<String> apiResponse =
                    restTemplate.exchange(
                        backendTestLoginUrl,
                            HttpMethod.GET,
                            entity,
                            String.class
                    );

            if (apiResponse.getStatusCode().is2xxSuccessful()) {
                System.out.println("Inicio de sesión autorizado");
            }

        } catch (HttpClientErrorException.Unauthorized e) {

            System.out.println("Token inválido o acceso no autorizado");

        } catch (Exception e) {

            System.out.println("Error al validar el token contra el backend");
        }
    }

}
