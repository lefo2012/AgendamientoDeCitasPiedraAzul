package Agendas.Entities;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

public class Usuario {

    @Getter @Setter
    private String correo;
    @Getter @Setter
    private String contrasenia;

    private List<String> roles;

    // de momento no es una entidad de DDD tiene que haber un uso en getter
    // y setter entidad anemica


    public Usuario(String correo, String contrasenia) {

    }

}
