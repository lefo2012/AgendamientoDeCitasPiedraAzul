package Agendas.Entities;

import java.time.DayOfWeek;
import java.util.*;

public class Agenda {

    Map<DayOfWeek,List<Intervalo>> disponibles;
    Map<DayOfWeek,List<Intervalo>> intervalosOcupados;



    public Agenda(){
        disponibles = new HashMap<>();
        intervalosOcupados = new HashMap<>();
    }

    public boolean agendar(DayOfWeek dia, Intervalo intervalo) {

        if (!estaDisponible(dia, intervalo)) {
            throw new IllegalArgumentException("INTERVALO NO DISPONIBLE");
        }

        intervalosOcupados.putIfAbsent(dia, new ArrayList<>());

        for (Intervalo ocupado : intervalosOcupados.get(dia)) {
            if (ocupado.seSolapa(intervalo)) {
                throw new IllegalArgumentException("INTERVALO OCUPADO");
            }
        }

        intervalosOcupados.get(dia).add(intervalo);

        return true;
    }

    private void reservar(DayOfWeek dia , Intervalo intervalo) throws Exception
    {

        if(!estaDisponible(dia,intervalo)){
            System.err.println("ERROR EL INTERVALO NO ESTA DISPONIBLE");
            throw new Exception("ERROR EL INTERVALO NO ESTA DISPONIBLE");
        }
        intervalosOcupados.get(dia).add(intervalo);
    }

    private boolean configurarAgenda(DayOfWeek dia , List<Intervalo> horario){

        if(disponibles == null){
            System.err.println("ERROR MAPA SIN INICIALIZAR REVISAR A MEDICO....  Iniciando mapa en configurar agenda ");
            disponibles = new HashMap<>();
        }

        disponibles.put(dia,horario);

        return true;
    }

    //Para que esta funcion funcione correctamente debe llegar en la misma posicion la lista de dias y intervalos
    //Ejemplo de uso {LUNES,MARTES} {{{11:00,11:30}{13:00,15:00}},{{12:00,13:00} {15:00,18:00}}}
    public boolean configurarAgenda(List<DayOfWeek> dias , List<List<Intervalo>> horarios)
    {
        if(dias.size() != horarios.size()){
            throw new IllegalArgumentException("Dias y horarios no coinciden");
        }

        for(int i = 0; i < dias.size(); i++){
            configurarAgenda(dias.get(i), horarios.get(i));
        }

        return true;
    }

    private boolean estaDisponible(DayOfWeek dia, Intervalo intervalo) {

        List<Intervalo> horarios = disponibles.get(dia);

        if (horarios == null) return false;

        for (Intervalo disponible : horarios) {
            if (intervalo.estaDentroDe(disponible)) {
                return true;
            }
        }

        return false;
    }





}
