package co.edu.unicauca.BackendPiedraAzul.Authentication.config;


import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String SESSION_CLIENT_HEADER = "X-Auth-Client";
    private static final String SESSION_CLIENT_DOCTOR = "doctor";
    private static final String SESSION_CLIENT_PATIENT = "patient";


    @Bean
        public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            BearerTokenResolver bearerTokenResolver,
            CorsConfigurationSource corsConfigurationSource
        ) throws Exception {

        JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter();

        scopes.setAuthorityPrefix("SCOPE_");

        Converter<Jwt, AbstractAuthenticationToken> converter = jwt -> {

            Collection<GrantedAuthority> authorities = new ArrayList<>(scopes.convert(jwt));

            // Extract realm roles
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null) {
                List<String> realmRoles = (List<String>) realmAccess.get("roles");
                if (realmRoles != null) {
                    realmRoles.forEach(role ->
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase(Locale.ROOT)))
                    );
                }
            }

            // Extract client roles
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess != null) {
                Map<String, Object> clientRoles = (Map<String, Object>) resourceAccess.get("piedraAzul-app");
                if (clientRoles != null) {
                    List<String> roles = (List<String>) clientRoles.get("roles");
                    if (roles != null) {
                        roles.forEach(role ->
                                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase(Locale.ROOT)))
                        );
                    }
                }
            }

            return new JwtAuthenticationToken(jwt, authorities);
        };

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()
                )
            .oauth2ResourceServer(oauth ->
                oauth
                    .bearerTokenResolver(bearerTokenResolver)
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(converter))
            )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public BearerTokenResolver bearerTokenResolver(
            @Value("${auth.cookie.access-name:PA_ACCESS}") String accessCookieName) {
        return request -> {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                return authorization.substring(7);
            }

            if (request.getCookies() == null) {
                return null;
            }

            String sessionClient = request.getHeader(SESSION_CLIENT_HEADER);
            String resolvedCookieName = resolveAccessCookieName(accessCookieName, sessionClient);

            for (Cookie cookie : request.getCookies()) {
                if (resolvedCookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }

            return null;
        };
    }

    private String resolveAccessCookieName(String baseCookieName, String sessionClient) {
        String normalizedClient = normalizeSessionClient(sessionClient);

        if (normalizedClient == null) {
            return baseCookieName;
        }

        return baseCookieName + "_" + normalizedClient.toUpperCase(Locale.ROOT);
    }

    private String normalizeSessionClient(String sessionClient) {
        if (sessionClient == null || sessionClient.isBlank()) {
            return null;
        }

        String normalized = sessionClient.trim().toLowerCase(Locale.ROOT);
        if (SESSION_CLIENT_DOCTOR.equals(normalized) || SESSION_CLIENT_PATIENT.equals(normalized)) {
            return normalized;
        }

        return null;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origins:http://localhost:4200,http://localhost:4300}") String allowedOrigins) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", SESSION_CLIENT_HEADER));
        configuration.setExposedHeaders(List.of("Set-Cookie"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}