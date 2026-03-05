package Agendas.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Persona {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;
    @Enumerated(EnumType.STRING)
    protected EnumTipoDoc tipoDeDocumento;
    protected String numeroIdentificacion;
    protected String nombre;
    protected String apellido;
    protected Date fechaNacimiento;
    protected String telefono;
    protected boolean activo;
    @OneToOne
    protected Usuario usuario;
}
