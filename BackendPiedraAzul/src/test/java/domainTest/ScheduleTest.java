package domainTest;

import Appointments.entities.Interval;
import Appointments.entities.IntervalList;
import Appointments.entities.Schedule;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ScheduleTest {
    /**
     * Placeholder test for Schedule functionality. To be implemented.
     */
    @Test
    void scheduleGetInstanceTest(){

        Schedule schedule;

        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        IntervalList interval = new IntervalList();

        int year = 2026;

        int weeksRepeat = 5;


        Interval interval1 = new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0));
        Interval interval2 = new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0));

        interval.addInterval(interval1);
        interval.addInterval(interval2);


        days.add(DayOfWeek.MONDAY);


        for(int i = 0; i<days.size();i++){

            intervals.add(interval);

        }

        schedule = new Schedule(days,intervals,weeksRepeat,year);
        schedule.print();
    }
    @Test
    void scheduleTrueTest(){
        Schedule schedule;

        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        IntervalList interval = new IntervalList();

        int year = 2026;

        int weeksRepeat = 5;


        Interval interval1 = new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0));
        Interval interval2 = new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0));

        interval.addInterval(interval1);
        interval.addInterval(interval2);


        days.add(DayOfWeek.MONDAY);


        for(int i = 0; i<days.size();i++){

            intervals.add(interval);

        }

        schedule = new Schedule(days,intervals,weeksRepeat,year);

        Interval reserveInterval = new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0));



        LocalDate reserveDay = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));


        /**
         * Reserve the next monday it is available
         * */
        assertTrue(schedule.schedule(reserveDay,reserveInterval));
    }

    @Test
    void scheduleFalseTest(){
        Schedule schedule;

        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        IntervalList interval = new IntervalList();

        int year = 2026;

        int weeksRepeat = 5;


        Interval interval1 = new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0));
        Interval interval2 = new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0));

        interval.addInterval(interval1);
        interval.addInterval(interval2);


        days.add(DayOfWeek.MONDAY);


        for(int i = 0; i<days.size();i++){

            intervals.add(interval);

        }

        schedule = new Schedule(days,intervals,weeksRepeat,year);

        Interval reserveInterval = new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0));



        LocalDate reserveDay = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));


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

            assertTrue(e.getMessage().contains("INTERVAL OCCUPIED"));
        }

    }
    @Test
    void scheduleTrueTwoReserveTest(){
        Schedule schedule;

        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        IntervalList interval = new IntervalList();

        int year = 2026;

        int weeksRepeat = 5;


        Interval interval1 = new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0));
        Interval interval2 = new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0));

        interval.addInterval(interval1);
        interval.addInterval(interval2);


        days.add(DayOfWeek.MONDAY);


        for(int i = 0; i<days.size();i++){

            intervals.add(interval);

        }

        schedule = new Schedule(days,intervals,weeksRepeat,year);

        Interval reserveInterval = new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0));
        Interval reserveInterval2 = new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0));


        LocalDate reserveDay = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));


        /**
         * Reserve the next monday it is available
         * */
        assertTrue(schedule.schedule(reserveDay,reserveInterval));

        assertTrue(schedule.schedule(reserveDay,reserveInterval2));


    }

    @Test
    void scheduleTrueNextDayReserveTest(){
        Schedule schedule;

        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        IntervalList interval = new IntervalList();

        int year = 2026;

        int weeksRepeat = 5;


        Interval interval1 = new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0));
        Interval interval2 = new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0));

        interval.addInterval(interval1);
        interval.addInterval(interval2);


        days.add(DayOfWeek.MONDAY);


        for(int i = 0; i<days.size();i++){

            intervals.add(interval);

        }

        schedule = new Schedule(days,intervals,weeksRepeat,year);

        Interval reserveInterval = new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0));


        LocalDate reserveDay = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));


        /**
         * Reserve the next monday it is available
         * */
        assertTrue(schedule.schedule(reserveDay,reserveInterval));

        /**
         * Next monday reserve
         * */
        reserveDay = reserveDay.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        assertTrue(schedule.schedule(reserveDay,reserveInterval));


    }



}
