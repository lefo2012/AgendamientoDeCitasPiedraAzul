package Appointments.domain;

import Appointments.utilities.HolidayUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class Schedule {

    private Long Id;
    private Set<LocalDate> holidays;
    private Map<LocalDate, IntervalList> availableTimes;
    private Map<LocalDate, IntervalList> busyTimes;

    public Schedule() {
        this.holidays = new HashSet<>();
        this.availableTimes = new HashMap<>();
        this.busyTimes = new HashMap<>();
    }

    public Schedule(Long id, Set<LocalDate> holidays, Map<LocalDate, IntervalList> availableTimes, Map<LocalDate, IntervalList> busyTimes) {
        Id = id;
        this.holidays = holidays;
        this.availableTimes = availableTimes;
        this.busyTimes = busyTimes;
    }

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

    //Repeat the calendar configuration for as many weeks as the user desires.
    /*
     * This function needs 3 parameters to work
     * This function configures the schedule by adding days to the available times
     * throughout the desired weeks, specifying which day the configuration will be repeated.
     * */
    private boolean configureSchedule(DayOfWeek day, IntervalList schedule, int weeksRepeat) {

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
    public void calculateHolidaysForYear(int year) {
        this.holidays = HolidayUtils.getHolidays(year);
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Set<LocalDate> getHolidays() {
        return holidays;
    }

    public void setHolidays(Set<LocalDate> holidays) {
        this.holidays = holidays;
    }

    public Map<LocalDate, IntervalList> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(Map<LocalDate, IntervalList> availableTimes) {
        this.availableTimes = availableTimes;
    }

    public Map<LocalDate, IntervalList> getBusyTimes() {
        return busyTimes;
    }

    public void setBusyTimes(Map<LocalDate, IntervalList> busyTimes) {
        this.busyTimes = busyTimes;
    }
}
