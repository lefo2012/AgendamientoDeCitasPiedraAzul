package Citas.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    @ElementCollection
    private Map<DayOfWeek, IntervaloList> horariosDisponibles;
    @ElementCollection
    private Map<LocalDate, IntervaloList> horariosOcupados;

    public boolean agendar(LocalDate dia, Intervalo intervalo) {

        if (!estaDisponible(dia.getDayOfWeek(), intervalo)) {
            throw new IllegalArgumentException("INTERVALO NO DISPONIBLE");
        }

        horariosOcupados.putIfAbsent(dia, new IntervaloList());

        for (Intervalo ocupado : horariosOcupados.get(dia).getIntervalos()) {
            if (ocupado.seSolapa(intervalo)) {
                throw new IllegalArgumentException("INTERVALO OCUPADO");
            }
        }

        horariosOcupados.get(dia).getIntervalos().add(intervalo);

        return true;
    }

    private void reservar(LocalDate dia, Intervalo intervalo) throws Exception {

        if (!estaDisponible(dia.getDayOfWeek(), intervalo)) {
            System.err.println("ERROR EL INTERVALO NO ESTA DISPONIBLE");
            throw new Exception("ERROR EL INTERVALO NO ESTA DISPONIBLE");
        }
        horariosOcupados.get(dia).getIntervalos().add(intervalo);
    }

    private boolean configurarAgenda(LocalDate dia, IntervaloList horario) {

        if (horariosDisponibles == null) {
            System.err.println("ERROR MAPA SIN INICIALIZAR REVISAR A MEDICO....  Iniciando mapa en configurar agenda ");
            horariosDisponibles = new HashMap<>();
        }

        horariosDisponibles.put(dia.getDayOfWeek(), horario);

        return true;
    }

    //Para que esta funcion funcione correctamente debe llegar en la misma posicion la lista de dias y intervalos
    //Ejemplo de uso {LUNES,MARTES} {{{11:00,11:30}{13:00,15:00}},{{12:00,13:00} {15:00,18:00}}}
    public boolean configurarAgenda(List<LocalDate> dias, List<IntervaloList> horarios) {
        if (dias.size() != horarios.size()) {
            throw new IllegalArgumentException("Dias y horarios no coinciden");
        }

        for (int i = 0; i < dias.size(); i++) {
            configurarAgenda(dias.get(i), horarios.get(i));
        }

        return true;
    }

    private boolean estaDisponible(DayOfWeek dia, Intervalo intervalo) {

        IntervaloList horarios = horariosDisponibles.get(dia);

        if (horarios == null) return false;

        for (Intervalo disponible : horarios.getIntervalos()) {
            if (intervalo.estaDentroDe(disponible)) {
                return true;
            }
        }

        return false;
    }


}
