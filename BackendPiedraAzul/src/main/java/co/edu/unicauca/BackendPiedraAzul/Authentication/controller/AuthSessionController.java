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
    public ResponseEntity<?> login(@RequestBody SessionLoginRequest request) {
        try {
            Map<String, Object> tokenResponse = tokenClient.requestPasswordToken(request.getUsername(), request.getPassword());
            return buildSessionResponse(tokenResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "invalid_credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "login_failed", "message", e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        String refreshToken = readCookie(request, refreshCookieName);
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "missing_refresh_token"));
        }

        try {
            Map<String, Object> tokenResponse = tokenClient.requestRefreshToken(refreshToken);
            return buildSessionResponse(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "refresh_failed", "message", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie accessCookie = buildCookie(accessCookieName, "", Duration.ZERO);
        ResponseCookie refreshCookie = buildCookie(refreshCookieName, "", Duration.ZERO);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(Map.of("status", "ok"));
    }

    private ResponseEntity<?> buildSessionResponse(Map<String, Object> tokenResponse) {
        String accessToken = asString(tokenResponse.get("access_token"));
        String refreshToken = asString(tokenResponse.get("refresh_token"));

        if (accessToken == null || accessToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "missing_access_token"));
        }

        Duration accessTtl = Duration.ofSeconds(asLong(tokenResponse.get("expires_in"), 300));
        Duration refreshTtl = Duration.ofSeconds(asLong(tokenResponse.get("refresh_expires_in"), 1800));

        ResponseCookie accessCookie = buildCookie(accessCookieName, accessToken, accessTtl);
        ResponseCookie refreshCookie = buildCookie(refreshCookieName, refreshToken, refreshTtl);

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString());

        if (refreshToken != null && !refreshToken.isBlank()) {
            responseBuilder.header(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        }

        return responseBuilder.body(Map.of("status", "ok"));
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
