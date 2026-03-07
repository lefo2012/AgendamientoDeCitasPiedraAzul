package domainTest;


import Appointments.entities.IntervalEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

public class IntervalTest {

    IntervalEntity interval;

    /**
     * Tests that isWithin returns true when the interval is completely within another interval.
     */
    @Test
    void isWithinTrueTest()
    {
        interval = new IntervalEntity(LocalTime.of(11,00),LocalTime.of(21,00));

        IntervalEntity interval1 = new IntervalEntity(LocalTime.of(11,00),LocalTime.of(21,00));

        Assertions.assertTrue(interval.isWithin(interval1));
    }

    /**
     * Tests that isWithin returns false when the interval is not completely within another interval.
     */
    @Test
    void isWithinFalseTest()
    {
        interval = new IntervalEntity(LocalTime.of(11,00),LocalTime.of(22,00));

        IntervalEntity interval1 = new IntervalEntity(LocalTime.of(11,00),LocalTime.of(21,00));

        Assertions.assertFalse(interval.isWithin(interval1));
    }

    /**
     * Tests that overlaps returns true when two intervals overlap.
     */
    @Test
    void overlapsTrueTest()
    {
        interval = new IntervalEntity(LocalTime.of(20,59),LocalTime.of(22,00));

        IntervalEntity interval1 = new IntervalEntity(LocalTime.of(11,00),LocalTime.of(21,00));

        Assertions.assertTrue(interval.overlaps(interval1));
    }

    /**
     * Tests that overlaps returns false when two intervals do not overlap.
     */
    @Test
    void overlapsFalseTest()
    {
        interval = new IntervalEntity(LocalTime.of(21,59),LocalTime.of(22,00));

        IntervalEntity interval1 = new IntervalEntity(LocalTime.of(11,00),LocalTime.of(21,00));

        Assertions.assertFalse(interval.overlaps(interval1));
    }

    /**
     * Tests that equals returns true for two identical intervals.
     */
    @Test
    void equalsTrueTest()
    {
        interval = new IntervalEntity(LocalTime.of(21,59),LocalTime.of(22,00));
        IntervalEntity interval1 = new IntervalEntity(LocalTime.of(21,59),LocalTime.of(22,00));
        assertEquals(interval, interval1);
    }

    /**
     * Tests that equals returns false for two different intervals.
     */
    @Test
    void equalsFalseTest()
    {

        interval = new IntervalEntity(LocalTime.of(21,58),LocalTime.of(22,00));
        IntervalEntity interval1 = new IntervalEntity(LocalTime.of(21,59),LocalTime.of(22,00));
        assertNotEquals(interval, interval1);
    }


}
