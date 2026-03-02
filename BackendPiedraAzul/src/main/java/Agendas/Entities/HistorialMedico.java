package Agendas.Entities;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class HistorialMedico {

    //Se usara un mapa para una busqueda por fecha de manera mas sencilla
    //No se como guardar en JPA los maps
    private Map<Date,String> historialMedico;

    @OneToOne
    private Paciente paciente;

    //Hay que mirar bien esto si es interesante o si lo podemos dejar solo en el historial como tal
    @OneToMany
    private List<Medico> medico;



}
