package domainTest;


import Appointments.entities.Interval;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

public class IntervalTest {

    Interval interval;

    @Test
    void isWithinTrueTest()
    {
        interval = new Interval(LocalTime.of(11,00),LocalTime.of(21,00));

        Interval interval1 = new Interval(LocalTime.of(11,00),LocalTime.of(21,00));

        Assertions.assertTrue(interval.isWithin(interval1));
    }

    @Test
    void isWithinFalseTest()
    {
        interval = new Interval(LocalTime.of(11,00),LocalTime.of(22,00));

        Interval interval1 = new Interval(LocalTime.of(11,00),LocalTime.of(21,00));

        Assertions.assertFalse(interval.isWithin(interval1));
    }
    @Test
    void overloadsTrueTest()
    {
        interval = new Interval(LocalTime.of(20,59),LocalTime.of(22,00));

        Interval interval1 = new Interval(LocalTime.of(11,00),LocalTime.of(21,00));

        Assertions.assertTrue(interval.overlaps(interval1));
    }
    @Test
    void overloadsFalseTest()
    {
        interval = new Interval(LocalTime.of(21,59),LocalTime.of(22,00));

        Interval interval1 = new Interval(LocalTime.of(11,00),LocalTime.of(21,00));

        Assertions.assertFalse(interval.overlaps(interval1));
    }
    @Test
    void equalsTrueTest()
    {
        interval = new Interval(LocalTime.of(21,59),LocalTime.of(22,00));
        Interval interval1 = new Interval(LocalTime.of(21,59),LocalTime.of(22,00));
        assertEquals(interval, interval1);
    }
    @Test
    void equalsFalseTest()
    {

        interval = new Interval(LocalTime.of(21,58),LocalTime.of(22,00));
        Interval interval1 = new Interval(LocalTime.of(21,59),LocalTime.of(22,00));
        assertNotEquals(interval, interval1);
    }


}
