package co.edu.unicauca.BackendPiedraAzul.Authentication.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello-1")
    public String hello()
    {
        return "hello";
    }
    @GetMapping("/roles")
    public List<String> roles(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(authority -> {
                    String authStr = authority.getAuthority();
                    return authStr != null ? authStr.replace("ROLE_", "") : "";
                })
                .collect(Collectors.toList());
    }
    @GetMapping("/client-roles")
    public List<String> getClientRoles(JwtAuthenticationToken auth) {

        Jwt jwt = auth.getToken();

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess == null) {
            return List.of();
        }

        Map<String, Object> client = (Map<String, Object>) resourceAccess.get("piedraAzul-app");

        if (client == null) {
            return List.of();
        }

        List<String> roles = (List<String>) client.get("roles");

        return roles != null ? roles : List.of();
    }
    @GetMapping("/hello-2")
    public String hello2()
    {
        return "hello pero 2";
    }

    @GetMapping("/token")
    public Map<String, Object> token(JwtAuthenticationToken auth) {
        return auth.getToken().getClaims();
    }

    /**
     * Endpoint accesible solo para usuarios con rol ADMIN
     */
    @PreAuthorize("hasRole('admin-client-role')")
    @GetMapping("/admin-only1")
    public String adminOnly()
    {
        return "Este endpoint es solo para administradores";
    }



    @PreAuthorize("hasRole('user')")
    @GetMapping("/admin-only4")
    public String adminOnly4() {
        return "Este endpoint es solo para juan HPTAAAAA";

    }


    @PreAuthorize("hasRole('admin')")
    @GetMapping("/admin-only2")
    public String adminOnly2()
    {
        return "Este endpoint es solo para administradores ZUNGOS";
    }

    @PreAuthorize("hasAuthority('ROLE_medico-client-role')")
    @GetMapping("/admin-only3")
    public String adminOnly3() {
        return "Este endpoint es solo para administradores HPTAAAAA";
    }
    /**
     * Endpoint accesible para usuarios con rol MEDICO
     */
    @PreAuthorize("hasRole('ROLE_medico')")
    @GetMapping("/medico-only")
    public String medicoOnly()
    {
        return "Este endpoint es solo para médicos";
    }

    /**
     * Endpoint accesible para usuarios con rol PACIENTE
     */
    @PreAuthorize("hasRole('PACIENTE')")
    @GetMapping("/paciente-only")
    public String pacienteOnly()
    {
        return "Este endpoint es solo para pacientes";
    }

    /**
     * Endpoint accesible para ADMIN y MEDICO
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/admin-medico")
    public String adminMedico()
    {
        return "Este endpoint es para administradores y médicos";
    }

    /**
     * Endpoint accesible para MEDICO y PACIENTE
     */
    @PreAuthorize("hasRole('MEDICO') or hasRole('PACIENTE')")
    @GetMapping("/medico-paciente")
    public String medicoPaciente()
    {
        return "Este endpoint es para médicos y pacientes";
    }

    /**
     * Endpoint accesible para cualquier usuario autenticado
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/authenticated")
    public String authenticated()
    {
        return "Este endpoint es para cualquier usuario autenticado";
    }
}
