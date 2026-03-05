package Citas.entities;

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
    private Medico medico;
    @ManyToOne
    private Paciente paciente;
    private Date fechaCita;
    @Enumerated(EnumType.STRING)
    private EnumEstadoCita estadoCita;

    /*
     * Funcion que crea una cita dentro del ambito de las entidades
     * */
    Cita agendarCita(Medico medico, Date fechaCita, Paciente paciente) {

        Cita cita = new Cita(medico, fechaCita, paciente);

        medico.addCitaPorAtender(cita);

        paciente.addCitasPendientes(cita);

        return cita;
    }

    private Cita(Medico medico, Date fechaCita, Paciente paciente) {
        this.medico = medico;
        this.fechaCita = fechaCita;
        this.paciente = paciente;
        this.estadoCita = EnumEstadoCita.EN_PROCESO;
    }

}
