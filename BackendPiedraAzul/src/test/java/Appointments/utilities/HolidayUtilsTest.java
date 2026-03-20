package Appointments.utilities;

import co.edu.unicauca.BackendPiedraAzul.Appointments.utilities.HolidayUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HolidayUtilsTest {

    /**
     * Tests that the generated Colombian holidays list is not null and not empty.
     */
    @Test
    void testGenerateColombianHolidaysNotEmpty() {
        List<LocalDate> holidays = HolidayUtils.generateColombianHolidays(2024);
        assertNotNull(holidays);
        assertFalse(holidays.isEmpty());
    }

    /**
     * Tests that the generated holidays list contains New Year's Day (January 1).
     */
    @Test
    void testGenerateColombianHolidaysContainsNewYear() {
        List<LocalDate> holidays = HolidayUtils.generateColombianHolidays(2024);
        assertTrue(holidays.contains(LocalDate.of(2024, 1, 1)));
    }

    /**
     * Tests that the generated holidays list contains Independence Day (July 20).
     */
    @Test
    void testGenerateColombianHolidaysContainsIndependenceDay() {
        List<LocalDate> holidays = HolidayUtils.generateColombianHolidays(2024);
        assertTrue(holidays.contains(LocalDate.of(2024, 7, 20)));
    }

    /**
     * Tests that the generated holidays list contains Christmas Day (December 25).
     */
    @Test
    void testGenerateColombianHolidaysContainsChristmas() {
        List<LocalDate> holidays = HolidayUtils.generateColombianHolidays(2024);
        assertTrue(holidays.contains(LocalDate.of(2024, 12, 25)));
    }

    /**
     * Tests that the generated holidays lists are different for different years.
     */
    @Test
    void testGenerateColombianHolidaysDifferentYears() {
        List<LocalDate> holidays2024 = HolidayUtils.generateColombianHolidays(2024);
        List<LocalDate> holidays2025 = HolidayUtils.generateColombianHolidays(2025);
        assertNotEquals(holidays2024, holidays2025);
    }

    /**
     * Tests that the generated holidays list has at least 18 holidays, as typical in Colombia.
     */
    @Test
    void testGenerateColombianHolidaysCount() {
        List<LocalDate> holidays = HolidayUtils.generateColombianHolidays(2024);
        // Colombia typically has around 18 holidays
        assertTrue(holidays.size() >= 18);
    }
}