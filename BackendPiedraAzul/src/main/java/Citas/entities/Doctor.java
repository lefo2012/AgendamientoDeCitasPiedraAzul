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
public class Doctor extends Persona{

    @ElementCollection
    private List<EnumEspecialidad> especialidades;
    @OneToMany
    private List<Appointment> citasProgramadas;
    @OneToMany
    private List<Appointment> citasAtendidas;
    //pensar en objeto de valor para que sea mas sencilla la distribucion de las fechas
    @OneToOne(cascade = CascadeType.ALL)
    private Agenda agenda;

    public boolean addCitaPorAtender(Appointment appointment) {
        if (citasProgramadas == null) {
            citasProgramadas = new ArrayList<>();
        }
        citasProgramadas.add(appointment);
        return true;

    }

}
