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
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private Doctor doctor;
    @ManyToOne
    private Paciente paciente;
    private Date fechaCita;
    @Enumerated(EnumType.STRING)
    private EnumEstadoCita estadoCita;

    /*
     * Funcion que crea una cita dentro del ambito de las entidades
     * */
    Appointment agendarCita(Doctor doctor, Date fechaCita, Paciente paciente) {

        Appointment appointment = new Appointment(doctor, fechaCita, paciente);

        doctor.addCitaPorAtender(appointment);

        paciente.addCitasPendientes(appointment);

        return appointment;
    }

    private Appointment(Doctor doctor, Date fechaCita, Paciente paciente) {
        this.doctor = doctor;
        this.fechaCita = fechaCita;
        this.paciente = paciente;
        this.estadoCita = EnumEstadoCita.EN_PROCESO;
    }

}
