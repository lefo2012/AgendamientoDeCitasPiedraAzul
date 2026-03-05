package Agendas.Entities;

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
public class Paciente extends Persona {

    @OneToMany
    private List<Cita> citasPendientes;
    @OneToMany
    private List<Cita> citasPasadas;
    @OneToOne
    private HistorialMedico historialMedico;
    private int cantidadDeCitas;

    public boolean addCitasPendientes(Cita cita) {

        if (citasPendientes == null) {
            citasPendientes = new ArrayList<>();
        }
        citasPendientes.add(cita);
        cantidadDeCitas++;

        return true;
    }

}
