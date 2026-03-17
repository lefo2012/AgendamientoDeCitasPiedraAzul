package domainTest;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Interval;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.IntervalList;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Schedule;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ScheduleTest {
    /**
     * Placeholder test for schedule domain functionality. To be implemented.
     */
    @Test
    void scheduleGetInstanceTest(){

        Schedule schedule;
        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        int year = 2026;
        int weeksRepeat = 5;
        days.add(DayOfWeek.MONDAY);


        for(int i = 0; i < days.size(); i++){

            IntervalList interval = new IntervalList();

            interval.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
            interval.addInterval(new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0)));

            intervals.add(interval);

        }

        schedule = new Schedule(days,intervals,weeksRepeat,year);
        schedule.print();
    }
    @Test
    void scheduleTrueTest() throws Exception {
        Schedule schedule;

        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        int year = 2026;

        int weeksRepeat = 5;

        days.add(DayOfWeek.MONDAY);

        for(int i = 0; i < days.size(); i++){

            IntervalList interval = new IntervalList();

            interval.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
            interval.addInterval(new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0)));

            intervals.add(interval);

        }

        schedule = new Schedule(days,intervals,weeksRepeat,year);

        Interval reserveInterval = new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0));
        LocalDate reserveDay = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        while(schedule.getHolidays().contains(reserveDay)){
            reserveDay = reserveDay.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }

        /**
         * Reserve the next monday it is available
         * */
        assertTrue(schedule.schedule(reserveDay,reserveInterval));
        schedule.print();
    }

    @Test
    void scheduleFalseTest() throws Exception {
        Schedule schedule;

        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        int year = 2026;

        int weeksRepeat = 5;

        days.add(DayOfWeek.MONDAY);

        for(int i = 0; i < days.size(); i++){

            IntervalList interval = new IntervalList();

            interval.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
            interval.addInterval(new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0)));

            intervals.add(interval);

        }




        schedule = new Schedule(days,intervals,weeksRepeat,year);

        Interval reserveInterval = new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0));



        LocalDate reserveDay = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        while(schedule.getHolidays().contains(reserveDay)){
            reserveDay = reserveDay.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }

        /**
         * Reserve the next monday it is available
         * */
        assertTrue(schedule.schedule(reserveDay,reserveInterval));

        /**
         * Reserve the next monday the same day before reserved
         * */

        try {
            schedule.schedule(reserveDay,reserveInterval);
        }catch(IllegalArgumentException e){

            assertTrue(e.getMessage().contains("INTERVAL NOT AVAILABLE"));
        }

    }
    @Test
    void scheduleTrueTwoReserveTest() throws Exception {
        Schedule schedule;

        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        int year = 2026;

        int weeksRepeat = 5;

        days.add(DayOfWeek.MONDAY);

        for(int i = 0; i < days.size(); i++){

            IntervalList interval = new IntervalList();

            interval.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
            interval.addInterval(new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0)));

            intervals.add(interval);

        }




        schedule = new Schedule(days,intervals,weeksRepeat,year);

        Interval reserveInterval = new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0));
        Interval reserveInterval2 = new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0));


        LocalDate reserveDay = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        while(schedule.getHolidays().contains(reserveDay)){
            reserveDay = reserveDay.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }

        /**
         * Reserve the next monday it is available
         * */
        assertTrue(schedule.schedule(reserveDay,reserveInterval));

        assertTrue(schedule.schedule(reserveDay,reserveInterval2));


    }

    @Test
    void scheduleTrueNextDayReserveTest() throws Exception {
        Schedule schedule;

        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        int year = 2026;

        int weeksRepeat = 5;

        days.add(DayOfWeek.MONDAY);

        for(int i = 0; i < days.size(); i++){

            IntervalList interval = new IntervalList();

            interval.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
            interval.addInterval(new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0)));

            intervals.add(interval);

        }


        schedule = new Schedule(days,intervals,weeksRepeat,year);

        Interval reserveInterval = new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0));


        LocalDate reserveDay = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        while(schedule.getHolidays().contains(reserveDay)){
            reserveDay = reserveDay.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }

        /**
         * Reserve the next monday it is available
         * */

        assertTrue(schedule.schedule(reserveDay,reserveInterval));

        /**
         * Next monday reserve
         * */

        reserveDay = reserveDay.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        while(schedule.getHolidays().contains(reserveDay)){
            reserveDay = reserveDay.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }

        assertTrue(schedule.schedule(reserveDay,reserveInterval));


    }


    @Test
    void scheduleTrueReserveInMiddleOfAContainerIntervalTest() throws Exception {
        Schedule schedule;

        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        int year = 2026;

        int weeksRepeat = 5;

        days.add(DayOfWeek.MONDAY);

        for (int i = 0; i < days.size(); i++) {

            IntervalList interval = new IntervalList();

            interval.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
            interval.addInterval(new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0)));

            intervals.add(interval);

        }


        schedule = new Schedule(days, intervals, weeksRepeat, year);

        Interval reserveInterval = new Interval(LocalTime.of(8, 0), LocalTime.of(9, 0));

        Interval reserveInterval2 = new Interval(LocalTime.of(10, 0), LocalTime.of(11, 0));
        LocalDate reserveDay = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        while (schedule.getHolidays().contains(reserveDay)) {
            reserveDay = reserveDay.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }

        assertTrue(schedule.schedule(reserveDay, reserveInterval));

        try {
            schedule.schedule(reserveDay, reserveInterval2);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("INTERVAL NOT AVAILABLE"));
        }
    }
    @Test
    void scheduleTrueReserveInMiddleOfAReserveTest() throws Exception {
        Schedule schedule;

        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        int year = 2026;

        int weeksRepeat = 5;

        days.add(DayOfWeek.MONDAY);

        for(int i = 0; i < days.size(); i++){

            IntervalList interval = new IntervalList();

            interval.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
            interval.addInterval(new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0)));

            intervals.add(interval);

        }


        schedule = new Schedule(days,intervals,weeksRepeat,year);

        Interval reserveInterval = new Interval(LocalTime.of(8, 0), LocalTime.of(9, 0));

        Interval reserveInterval2 = new Interval(LocalTime.of(8, 0), LocalTime.of(10, 0));
        LocalDate reserveDay = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        while(schedule.getHolidays().contains(reserveDay)){
            reserveDay = reserveDay.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }

        assertTrue(schedule.schedule(reserveDay,reserveInterval));

        try {
            schedule.schedule(reserveDay,reserveInterval2);
        }catch(IllegalArgumentException e){
            assertTrue(e.getMessage().contains("INTERVAL NOT AVAILABLE"));
        }

    }

    @Test
    void scheduleCancelTrue() throws Exception {
        Schedule schedule;

        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        int year = 2026;

        int weeksRepeat = 5;

        days.add(DayOfWeek.MONDAY);

        for(int i = 0; i < days.size(); i++){

            IntervalList interval = new IntervalList();

            interval.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
            interval.addInterval(new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0)));

            intervals.add(interval);

        }


        schedule = new Schedule(days,intervals,weeksRepeat,year);
        Interval reserveInterval = new Interval(LocalTime.of(8, 0), LocalTime.of(9, 0));
        Interval reserveInterval1 = new Interval(LocalTime.of(9, 0), LocalTime.of(10, 0));
        LocalDate reserveDay = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        while(schedule.getHolidays().contains(reserveDay)){
            reserveDay = reserveDay.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }



        assertTrue(schedule.schedule(reserveDay,reserveInterval));
        assertTrue(schedule.schedule(reserveDay,reserveInterval1));


        assertTrue(schedule.cancel(reserveDay,reserveInterval));



    }




}
