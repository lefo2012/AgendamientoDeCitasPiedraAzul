package Agendas.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private Medico medicoEncargado;
    @ManyToOne
    private Paciente paciente;
    private Date fechaCita;
    private EstadoCita estadoCita;

    /*
     * Funcion que crea una cita dentro del ambito de las entidades
     * */
    Cita agendarCita(Medico medicoEncargado, Date fechaCita, Paciente paciente) throws Exception {

        Cita cita = new Cita(medicoEncargado, fechaCita, paciente);

        medicoEncargado.addCitaPorAtender(cita);

        paciente.addCitasPendientes(cita);

        return cita;
    }

    private Cita(Medico medicoEncargado, Date fechaCita, Paciente paciente) {
        this.medicoEncargado = medicoEncargado;
        this.fechaCita = fechaCita;
        this.paciente = paciente;
        this.estadoCita = EstadoCita.EN_PROCESO;
    }

}
