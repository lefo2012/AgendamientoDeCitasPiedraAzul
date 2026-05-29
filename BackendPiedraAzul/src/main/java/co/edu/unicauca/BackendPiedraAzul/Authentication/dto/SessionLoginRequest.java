package co.edu.unicauca.BackendPiedraAzul.Authentication.dto;

import lombok.Getter;
import lombok.Setter;

public class SessionLoginRequest {

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;
}
