package Agendas.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.ArrayList;
import java.util.List;
@Entity
public class Paciente {

    private Usuario usuario;

    private int CC;
    private String nombre;
    private String apellido;

    //Objeto de valor para los medicos
    @OneToOne
    private HistorialMedico historialMedico;

    //Parametro intocable ....... mentira hay que ver en don va esto
    private int MAXIMODECITAS = 3;

    private int cantidadDeCitas;

    @OneToMany
    private List<Cita> citasPendientes;

    @OneToMany
    private List<Cita> citasPasadas;


    public boolean addCitasPendientes(Cita cita) throws Exception {

        if(cantidadDeCitas>=MAXIMODECITAS)
        {
            throw new Exception("El paciente supero la maxima cantidad de citas agendadas " + MAXIMODECITAS);
        }
        if(citasPendientes==null)
        {
            citasPendientes = new ArrayList<>();
        }

        citasPendientes.add(cita);
        cantidadDeCitas++;
        return true;
    }



}
