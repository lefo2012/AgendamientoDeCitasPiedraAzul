package co.edu.unicauca.BackendPiedraAzul.Appointments.domain;

import co.edu.unicauca.BackendPiedraAzul.Appointments.utilities.HolidayUtils;

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

    public Schedule(List<DayOfWeek> days, List<IntervalList> schedules, int weeksRepeat,int year) {
        setHolidaysForYear(year);
        this.availableTimes = new HashMap<>();
        this.busyTimes = new HashMap<>();
        configureSchedule(days, schedules, weeksRepeat);
    }

    public Schedule(Long id, Set<LocalDate> holidays, Map<LocalDate, IntervalList> availableTimes, Map<LocalDate, IntervalList> busyTimes) {
        Id = id;
        this.holidays = holidays;
        this.availableTimes = availableTimes;
        this.busyTimes = busyTimes;
    }

    public boolean schedule(LocalDate day, Interval interval) throws Exception {

        if (isAvailable(day, interval) == null) {
            throw new IllegalArgumentException("INTERVAL NOT AVAILABLE");
        }

        IntervalList busyIntervalsForDay = busyTimes.computeIfAbsent(day, keyDay -> {
            IntervalList intervalList = new IntervalList();
            intervalList.setDate(keyDay);
            return intervalList;
        });

        if (busyIntervalsForDay.getDate() == null) {
            busyIntervalsForDay.setDate(day);
        }

        for (Interval busy : busyIntervalsForDay.getIntervals()) {
            if (busy.overlaps(interval)) {
                throw new IllegalArgumentException("INTERVAL OCCUPIED");
            }
        }

        modifyAvailable(day,interval);
        busyIntervalsForDay.getIntervals().add(interval);

        return true;
    }

    private void reserve(LocalDate day, Interval interval) throws Exception {

        if (isAvailable(day, interval) == null) {
            throw new Exception("ERROR THE INTERVAL IS NOT AVAILABLE");
        }
        busyTimes.get(day).getIntervals().add(interval);
    }

    private boolean modifyAvailable(LocalDate day, Interval interval) throws Exception {

        Interval container = isAvailable(day, interval);

            if (container != null) {


                if(container.equals(interval)) {
                    availableTimes.get(day).getIntervals().remove(container);
                    return true;
                }
                availableTimes.get(day).getIntervals().add(new Interval(container.getStartTime(), interval.getStartTime()));
                availableTimes.get(day).getIntervals().add(new Interval(interval.getEndTime(), container.getEndTime()));

                availableTimes.get(day).getIntervals().remove(container);
                return true;

            }else  {
                throw new Exception("ERROR THE INTERVAL IS NOT AVAILABLE");
            }
    }
    public boolean cancel(LocalDate day, Interval interval) {

        if (!busyTimes.containsKey(day)) {
            return false;
        }

        if (!busyTimes.get(day).getIntervals().remove(interval)) {


            return false;
        }
        if(busyTimes.get(day).getIntervals().isEmpty()) {
            busyTimes.remove(day);
        }
        Interval left = null;
        Interval right = null;

        for (Interval i : availableTimes.get(day).getIntervals()) {

            if (i.getEndTime().equals(interval.getStartTime())) {
                left = i;
            }

            if (i.getStartTime().equals(interval.getEndTime())) {
                right = i;
            }
        }

        if (left != null && right != null) {
            left.setEndTime(right.getEndTime());
            availableTimes.get(day).getIntervals().remove(right);
        }
        else if (left != null) {
            left.setEndTime(interval.getEndTime());
        }
        else if (right != null) {
            right.setStartTime(interval.getStartTime());
        }
        else {
            availableTimes.get(day).getIntervals().add(interval);
        }

        return true;
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

        for(int i = 0; i < weeksRepeat; i++) {

            if(!holidays.contains(dayOfTheWeek)){

                IntervalList copy = new IntervalList();
                copy.setDate(dayOfTheWeek);

                for(Interval interval : schedule.getIntervals()){
                    copy.addInterval(new Interval(interval.getStartTime(), interval.getEndTime()));
                }

                availableTimes.put(dayOfTheWeek, copy);
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

    private Interval isAvailable(LocalDate day, Interval interval) {

        IntervalList schedules = availableTimes.get(day);

        if (schedules == null)
            return null;

        for (Interval available : schedules.getIntervals()) {
            if (interval.isWithin(available)) {
                return available;
            }
        }

        return null;
    }

    /**
     * Sets the list of holidays for a specific year using the automatic generator.
     *
     * @param year The year for which to generate the holidays.
     */
    private void setHolidaysForYear(int year) {
        this.holidays = HolidayUtils.getHolidays(year);
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

        System.out.println("AvailableTimes: ");
        for(LocalDate dia : availableTimes.keySet())
        {
            System.out.println(dia);
            availableTimes.get(dia).print();
        }
        System.out.println("BusyTimes: ");
        for(LocalDate dia : busyTimes.keySet())
        {
            System.out.println(dia);
            busyTimes.get(dia).print();
        }
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
