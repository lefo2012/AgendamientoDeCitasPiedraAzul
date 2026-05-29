package co.edu.unicauca.BackendPiedraAzul.Authentication.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KeycloakTokenClient {

    @Value("${keycloak.token-url}")
    private String keycloakTokenUrl;

    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.client-secret}")
    private String keycloakClientSecret;

    public Map<String, Object> requestPasswordToken(String username, String password) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", keycloakClientId);
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);
        if (keycloakClientSecret != null && !keycloakClientSecret.isBlank()) {
            body.add("client_secret", keycloakClientSecret);
        }
        return executeTokenRequest(body);
    }

    public Map<String, Object> requestRefreshToken(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", keycloakClientId);
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);
        if (keycloakClientSecret != null && !keycloakClientSecret.isBlank()) {
            body.add("client_secret", keycloakClientSecret);
        }
        return executeTokenRequest(body);
    }

    private Map<String, Object> executeTokenRequest(MultiValueMap<String, String> body) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(keycloakTokenUrl, request, Map.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalStateException("Keycloak token request failed with status " + response.getStatusCode());
            }
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new IllegalArgumentException("Invalid credentials");
            }
            throw e;
        }
    }
}
