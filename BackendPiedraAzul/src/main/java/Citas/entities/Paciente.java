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
public class Paciente extends Persona {

    @OneToMany
    private List<Appointment> citasPendientes;
    @OneToMany
    private List<Appointment> citasPasadas;
    @OneToOne
    private HistorialMedico historialMedico;
    private int cantidadDeCitas;

    public boolean addCitasPendientes(Appointment appointment) {

        if (citasPendientes == null) {
            citasPendientes = new ArrayList<>();
        }
        citasPendientes.add(appointment);
        cantidadDeCitas++;

        return true;
    }

}
