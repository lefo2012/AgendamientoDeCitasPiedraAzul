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
@NoArgsConstructor
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    private Set<LocalDate> holidays = new HashSet<>();

    @ElementCollection
    private Map<LocalDate, IntervalListEntity> availableTimes;

    @ElementCollection
    private Map<LocalDate, IntervalListEntity> busyTimes;

    public boolean schedule(LocalDate day, IntervalEntity interval) {

        if (!isAvailable(day, interval)) {
            throw new IllegalArgumentException("INTERVAL NOT AVAILABLE");
        }

        busyTimes.putIfAbsent(day, new IntervalListEntity());

        for (IntervalEntity busy : busyTimes.get(day).getIntervals()) {
            if (busy.overlaps(interval)) {
                throw new IllegalArgumentException("INTERVAL OCCUPIED");
            }
        }

        busyTimes.get(day).getIntervals().add(interval);

        return true;
    }

    private void reserve(LocalDate day, IntervalEntity interval) throws Exception {

        if (!isAvailable(day, interval)) {
            throw new Exception("ERROR THE INTERVAL IS NOT AVAILABLE");
        }
        busyTimes.get(day).getIntervals().add(interval);
    }

    //Repeat the calendar configuration for as many weeks as the user desires.
    /*
    * This function needs 3 parameters to work
    * This function configures the schedule by adding days to the available times
    * throughout the desired weeks, specifying which day the configuration will be repeated.
    * */
    private boolean configureSchedule(DayOfWeek day, IntervalListEntity schedule, int weeksRepeat) {

        if (availableTimes == null) {
            System.err.println("ERROR MAP NOT INITIALIZED CHECK DOCTOR.... Initializing map in configure schedule ");
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
    public boolean configureSchedule(List<DayOfWeek> days, List<IntervalListEntity> schedules, int weeksRepeat) {

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

    private boolean isAvailable(LocalDate day, IntervalEntity interval) {

        IntervalListEntity schedules = availableTimes.get(day);

        if (schedules == null)
            return false;

        for (IntervalEntity available : schedules.getIntervals()) {
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
    public void setHolidaysForYear(int year) {
        this.holidays = HolidayUtils.getHolidays(year);
    }

}
