package Citas.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Medico extends Persona{

    @ElementCollection
    private List<EnumEspecialidad> especialidades;
    @OneToMany
    private List<Cita> citasProgramadas;
    @OneToMany
    private List<Cita> citasAtendidas;
    //pensar en objeto de valor para que sea mas sencilla la distribucion de las fechas
    @OneToOne(cascade = CascadeType.ALL)
    private Agenda agenda;

    public boolean addCitaPorAtender(Cita cita) {
        if (citasProgramadas == null) {
            citasProgramadas = new ArrayList<>();
        }
        citasProgramadas.add(cita);
        return true;

    }

}
