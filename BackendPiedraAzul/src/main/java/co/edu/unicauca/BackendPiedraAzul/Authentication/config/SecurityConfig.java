package co.edu.unicauca.BackendPiedraAzul.Authentication.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

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
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + role))
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
                                authorities.add(new SimpleGrantedAuthority("ROLE_" + role))
                        );
                    }
                }
            }

            return new JwtAuthenticationToken(jwt, authorities);
        };

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(converter))
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}