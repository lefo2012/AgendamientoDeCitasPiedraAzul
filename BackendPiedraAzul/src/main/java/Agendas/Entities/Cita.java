package Agendas.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter


@Entity
public class Cita {


    private float id;

    @ManyToOne
    private Medico medicoEncargado;

    //Lo considero a futuro puede que no se use ni siquiera
    @Transient
    private Medico medicoAuxiliar;

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
    private Cita(Medico medicoEncargado, Date fechaCita, Paciente paciente)
    {
        this.medicoEncargado = medicoEncargado;
        this.fechaCita = fechaCita;
        this.paciente = paciente;
        this.estadoCita = EstadoCita.EN_PROCESO;
    }

}
