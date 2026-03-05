package Citas.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class HistorialMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    //Se usara un mapa para una busqueda por fecha de manera mas sencilla
    //No se como guardar en JPA los maps
    @ElementCollection
    private Map<Date, String> historialMedico;
    //Hay que mirar bien esto si es interesante o si lo podemos dejar solo en el historial como tal
    @OneToMany
    private List<Medico> medico;


}
