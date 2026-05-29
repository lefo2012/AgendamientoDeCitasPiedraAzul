package co.edu.unicauca.BackendPiedraAzul.Users.domain;

import java.util.List;


public class User {
    private Long id;
    private String email;
    private String keycloakId;
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getKeycloakId()
    {return keycloakId; }

    public void setKeycloakId(String keycloakId)
    {
        this.keycloakId = keycloakId;
    }
}
