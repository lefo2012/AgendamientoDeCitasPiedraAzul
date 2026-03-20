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

import java.util.Map;



@Component
public class LoginValidation {

    public void testLoginFlow() {

        RestTemplate restTemplate = new RestTemplate();
        String keycloakUrl =
                "http://localhost:8080/realms/piedraAzul-dev/protocol/openid-connect/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", "piedraAzul-app");
        body.add("grant_type", "password");
        body.add("username", "macarela");
        body.add("password", "1234");
        body.add("client_secret", "HFn9D3q4cLaZyLfTcs7h4J4cDLLLaRLh");

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);
        ResponseEntity<Map> response;
        try {
            response = restTemplate.postForEntity(keycloakUrl, request, Map.class);

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
                            "http://localhost:8081/test/login-test",
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
