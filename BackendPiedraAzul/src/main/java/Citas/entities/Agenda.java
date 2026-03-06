package Citas.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.time.temporal.TemporalAdjusters;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    private List<LocalDate> holidays = new ArrayList<>();

    @ElementCollection
    private Map<LocalDate, IntervaloList> availableTimes;

    @ElementCollection
    private Map<LocalDate, IntervaloList> busyTimes;

    public boolean schedule(LocalDate day, Intervalo intervalo) {

        if (!isAvailable(day, intervalo)) {
            throw new IllegalArgumentException("INTERVALO NO DISPONIBLE");
        }

        busyTimes.putIfAbsent(day, new IntervaloList());

        for (Intervalo busy : busyTimes.get(day).getIntervalos()) {
            if (busy.overlaps(intervalo)) {
                throw new IllegalArgumentException("INTERVALO OCUPADO");
            }
        }

        busyTimes.get(day).getIntervalos().add(intervalo);

        return true;
    }

    private void reserve(LocalDate day, Intervalo intervalo) throws Exception {

        if (!isAvailable(day, intervalo)) {
            throw new Exception("ERROR EL INTERVALO NO ESTA DISPONIBLE");
        }
        busyTimes.get(day).getIntervalos().add(intervalo);
    }

    //Repeat the calendar configuration for as many weeks as the user desires.
    /*
    * This funtion needs 3 parametres for work
    * This function configures the schedule by adding days to the available times
    * throughout the desired weeks, specifying which day the configuration will be repeated.
    * */
    private boolean configureSchedule(DayOfWeek day, IntervaloList schedule, int weeksRepeat) {

        if (availableTimes == null) {
            System.err.println("ERROR MAPA SIN INICIALIZAR REVISAR A MEDICO....  Iniciando mapa en configurar agenda ");
            availableTimes = new HashMap<>();
        }
        LocalDate today = LocalDate.now();
        LocalDate dayOfTheWeek = today.with(TemporalAdjusters.next(day));
        for(int i = 0; i < weeksRepeat; i++)
        {

            if(!holidays.contains(dayOfTheWeek)){
                availableTimes.put(dayOfTheWeek, schedule);
            }

            dayOfTheWeek = dayOfTheWeek.plusWeeks(1);
        }
        return true;
    }

    //For this function to work correctly, the list of days and intervals must arrive in the same position.
    //Example of use: {MONDAY,TUESDAY} {{{11:00,11:30}{13:00,15:00}},{{12:00,13:00} {15:00,18:00}}}
    public boolean configureSchedule(List<DayOfWeek> days, List<IntervaloList> schedules, int weeksRepeat) {

        if(weeksRepeat <= 0)
        {
            throw new IllegalArgumentException("La cantidad de semanas tiene que ser superior o igual a 1");
        }

        if (days.size() != schedules.size()) {
            throw new IllegalArgumentException("Cantidad de Dias y horarios no coinciden");
        }

        for (int i = 0; i < days.size(); i++) {
            configureSchedule(days.get(i), schedules.get(i), weeksRepeat);
        }

        return true;
    }

    private boolean isAvailable(LocalDate day, Intervalo interval) {

        IntervaloList schedules = availableTimes.get(day);

        if (schedules == null)
            return false;

        for (Intervalo available : schedules.getIntervalos()) {
            if (interval.isInOf(available)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Sets the list of holidays for a specific year using the automatic generator.
     *
     * @param year The year for which to generate the holidays.
     */
    public void setHolidaysForYear(int year) {
        this.holidays = HolidayUtils.generateColombianHolidays(year);
    }

}
