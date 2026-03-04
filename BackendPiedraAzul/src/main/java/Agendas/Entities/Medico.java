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
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Usuario usuario;
    private int CC;
    private String nombre;
    private String apellido;
    private List<String> Especialidad;
    private List<Cita> citasPorAtender;
    private List<Cita> citasAtendidas;

    //Pensar si va a ser util realmente
    private List<Paciente> pacientesAtendidos;
    //pensar en objeto de valor para que sea mas sencilla la distribucion de las fechas
    @OneToOne(cascade = CascadeType.ALL)
    private Agenda agenda;

    public boolean addCitaPorAtender(Cita cita) {
        if (citasPorAtender == null) {
            citasPorAtender = new ArrayList<>();
        }
        citasPorAtender.add(cita);
        return true;

    }

}
