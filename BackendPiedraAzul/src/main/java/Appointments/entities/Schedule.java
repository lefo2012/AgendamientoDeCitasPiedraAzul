package Appointments.entities;

import Appointments.utilities.HolidayUtils;
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
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    private List<LocalDate> holidays;

    @ElementCollection
    private Map<LocalDate, IntervalList> availableTimes;

    @ElementCollection
    private Map<LocalDate, IntervalList> busyTimes;

    public Schedule(List<DayOfWeek> days, List<IntervalList> schedules, int weeksRepeat,int year) {
        setHolidaysForYear(year);
        configureSchedule(days, schedules, weeksRepeat);

        busyTimes = new HashMap<>();
    }
    /**
     * Generate instance for a Schedule, is obligatory initialize all agenda
     * @Param days
     * @Param schedules
     * @Param weeksRepeat
     * @Param year for the holidays in the year
    * */


    public boolean schedule(LocalDate day, Interval interval) {

        if (!isAvailable(day, interval)) {
            throw new IllegalArgumentException("INTERVAL NOT AVAILABLE");
        }

        busyTimes.putIfAbsent(day, new IntervalList());

        for (Interval busy : busyTimes.get(day).getIntervals()) {
            if (busy.overlaps(interval)) {
                throw new IllegalArgumentException("INTERVAL OCCUPIED");
            }
        }

        busyTimes.get(day).getIntervals().add(interval);

        return true;
    }

    private void reserve(LocalDate day, Interval interval) throws Exception {

        if (!isAvailable(day, interval)) {
            throw new Exception("ERROR THE INTERVAL IS NOT AVAILABLE");
        }
        busyTimes.get(day).getIntervals().add(interval);
    }

    /**
    * This function needs 3 parameters to work
    * This function configures the schedule by adding days to the available times
    * throughout the desired weeks, specifying which day the configuration will be repeated.
    * */
    private boolean configureSchedule(DayOfWeek day, IntervalList schedule, int weeksRepeat) {

        if (availableTimes == null) {
            System.err.println("ERROR MAP OF AVAILABLE TIMES NOT INITIALIZED... CHECKING.... Initializing map in configure schedule ");
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

    /**
    * For this function to work correctly, the list of days and intervals must arrive in the same position.
    * Example of use: {MONDAY,TUESDAY} {{{11:00,11:30}{13:00,15:00}},{{12:00,13:00} {15:00,18:00}}}
    */
    public boolean configureSchedule(List<DayOfWeek> days, List<IntervalList> schedules, int weeksRepeat) {

        if(weeksRepeat <= 0)
        {
            throw new IllegalArgumentException("The number of weeks must be greater than or equal to 1");
        }

        if (days.size() != schedules.size()) {
            throw new IllegalArgumentException("Number of days and schedules do not match");
        }

        for (int i = 0; i < days.size(); i++) {
            configureSchedule(days.get(i), schedules.get(i), weeksRepeat);
        }

        return true;
    }

    private boolean isAvailable(LocalDate day, Interval interval) {

        IntervalList schedules = availableTimes.get(day);

        if (schedules == null)
            return false;

        for (Interval available : schedules.getIntervals()) {
            if (interval.isWithin(available)) {
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
    private void setHolidaysForYear(int year) {
        this.holidays = HolidayUtils.generateColombianHolidays(year);
    }

    /**
     *
     * This function is for print on console the schedule
     * */

    public void print()
    {
        if(availableTimes == null && busyTimes == null && holidays == null)
        {

            throw new IllegalArgumentException("ERROR THE INFORMATION IS NOT AVAILABLE");

        }

        System.out.println("Holidays: ");
        for (LocalDate day : holidays)
        {
            System.out.println(day);
        }
        System.out.println("AvailableTimes: ");
        for(LocalDate dia : availableTimes.keySet())
        {
            System.out.println(dia);
            availableTimes.get(dia).print();
        }
    }

}
