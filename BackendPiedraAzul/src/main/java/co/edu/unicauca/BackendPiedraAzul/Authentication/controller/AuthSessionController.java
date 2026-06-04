package co.edu.unicauca.BackendPiedraAzul.Authentication.controller;

import co.edu.unicauca.BackendPiedraAzul.Authentication.dto.SessionLoginRequest;
import co.edu.unicauca.BackendPiedraAzul.Authentication.services.KeycloakTokenClient;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/session")
public class AuthSessionController {

    private static final String SESSION_CLIENT_HEADER = "X-Auth-Client";
    private static final String SESSION_CLIENT_DOCTOR = "doctor";
    private static final String SESSION_CLIENT_PATIENT = "patient";

    private final KeycloakTokenClient tokenClient;

    @Value("${auth.cookie.access-name:PA_ACCESS}")
    private String accessCookieName;

    @Value("${auth.cookie.refresh-name:PA_REFRESH}")
    private String refreshCookieName;

    @Value("${auth.cookie.same-site:Lax}")
    private String sameSite;

    @Value("${auth.cookie.secure:false}")
    private boolean secure;

    @Value("${auth.cookie.path:/}")
    private String cookiePath;

    @Value("${auth.cookie.domain:}")
    private String cookieDomain;

    public AuthSessionController(KeycloakTokenClient tokenClient) {
        this.tokenClient = tokenClient;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestHeader(value = SESSION_CLIENT_HEADER, required = false) String sessionClient,
            @RequestBody SessionLoginRequest request) {
        try {
            Map<String, Object> tokenResponse = tokenClient.requestPasswordToken(request.getUsername(), request.getPassword());
            return buildSessionResponse(tokenResponse, sessionClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "invalid_credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "login_failed", "message", e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestHeader(value = SESSION_CLIENT_HEADER, required = false) String sessionClient,
            HttpServletRequest request) {
        String refreshToken = readCookie(request, getRefreshCookieName(sessionClient));
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "missing_refresh_token"));
        }

        try {
            Map<String, Object> tokenResponse = tokenClient.requestRefreshToken(refreshToken);
            return buildSessionResponse(tokenResponse, sessionClient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "refresh_failed", "message", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = SESSION_CLIENT_HEADER, required = false) String sessionClient) {
        ResponseCookie accessCookie = buildCookie(getAccessCookieName(sessionClient), "", Duration.ZERO);
        ResponseCookie refreshCookie = buildCookie(getRefreshCookieName(sessionClient), "", Duration.ZERO);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(Map.of("status", "ok"));
    }

    private ResponseEntity<?> buildSessionResponse(Map<String, Object> tokenResponse, String sessionClient) {
        String accessToken = asString(tokenResponse.get("access_token"));
        String refreshToken = asString(tokenResponse.get("refresh_token"));

        if (accessToken == null || accessToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "missing_access_token"));
        }

        Duration accessTtl = Duration.ofSeconds(asLong(tokenResponse.get("expires_in"), 300));
        Duration refreshTtl = Duration.ofSeconds(asLong(tokenResponse.get("refresh_expires_in"), 1800));

        ResponseCookie accessCookie = buildCookie(getAccessCookieName(sessionClient), accessToken, accessTtl);
        ResponseCookie refreshCookie = buildCookie(getRefreshCookieName(sessionClient), refreshToken, refreshTtl);

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString());

        if (refreshToken != null && !refreshToken.isBlank()) {
            responseBuilder.header(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        }

        return responseBuilder.body(Map.of("status", "ok"));
    }

    private String getAccessCookieName(String sessionClient) {
        return buildCookieName(accessCookieName, sessionClient);
    }

    private String getRefreshCookieName(String sessionClient) {
        return buildCookieName(refreshCookieName, sessionClient);
    }

    private String buildCookieName(String baseName, String sessionClient) {
        String normalizedClient = normalizeSessionClient(sessionClient);

        if (normalizedClient == null) {
            return baseName;
        }

        return baseName + "_" + normalizedClient.toUpperCase();
    }

    private String normalizeSessionClient(String sessionClient) {
        if (sessionClient == null || sessionClient.isBlank()) {
            return null;
        }

        String normalized = sessionClient.trim().toLowerCase();
        if (SESSION_CLIENT_DOCTOR.equals(normalized) || SESSION_CLIENT_PATIENT.equals(normalized)) {
            return normalized;
        }

        return null;
    }

    private ResponseCookie buildCookie(String name, String value, Duration maxAge) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path(cookiePath)
                .maxAge(maxAge);

        if (cookieDomain != null && !cookieDomain.isBlank()) {
            builder.domain(cookieDomain);
        }

        return builder.build();
    }

    private String readCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private long asLong(Object value, long defaultValue) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value instanceof String text) {
            try {
                return Long.parseLong(text);
            } catch (NumberFormatException ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
