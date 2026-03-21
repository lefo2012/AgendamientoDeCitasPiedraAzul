package Appointments.utilities;

import co.edu.unicauca.BackendPiedraAzul.Appointments.utilities.HolidayUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HolidayUtilsTest {
    /**
     * Tests that the generated Colombian holidays set is not null and not empty.
     */
    @Test
    void shouldGenerateNonEmptyHolidaySet() {
        Set<LocalDate> holidays = HolidayUtils.getHolidays(2024);

        assertNotNull(holidays);
        assertFalse(holidays.isEmpty());
    }
    /**
     * Tests fixed holidays that should always exist.
     */
    @Test
    void shouldContainFixedHolidays() {

        Set<LocalDate> holidays = HolidayUtils.getHolidays(2024);

        assertAll(
                () -> assertTrue(holidays.contains(LocalDate.of(2024, 1, 1))),  // Año nuevo
                () -> assertTrue(holidays.contains(LocalDate.of(2024, 5, 1))),  // Trabajo
                () -> assertTrue(holidays.contains(LocalDate.of(2024, 7, 20))), // Independencia
                () -> assertTrue(holidays.contains(LocalDate.of(2024, 12, 25))) // Navidad
        );
    }

    /**
     * Tests that Holy Week holidays are correctly generated.
     */
    @Test
    void shouldContainHolyWeekHolidays() {

        Set<LocalDate> holidays = HolidayUtils.getHolidays(2024);

        // Semana Santa 2024
        LocalDate holyThursday = LocalDate.of(2024, 3, 28);
        LocalDate goodFriday = LocalDate.of(2024, 3, 29);

        assertAll(
                () -> assertTrue(holidays.contains(holyThursday)),
                () -> assertTrue(holidays.contains(goodFriday))
        );
    }

    /**
     * Tests that different years generate different holiday sets.
     */
    @Test
    void shouldGenerateDifferentHolidaysForDifferentYears() {

        Set<LocalDate> holidays2024 = HolidayUtils.getHolidays(2024);
        Set<LocalDate> holidays2025 = HolidayUtils.getHolidays(2025);

        assertNotEquals(holidays2024, holidays2025);
    }

    /**
     * Tests the approximate number of Colombian holidays.
     */
    @Test
    void shouldHaveValidHolidayCount() {

        Set<LocalDate> holidays = HolidayUtils.getHolidays(2024);

        // Colombia tiene normalmente entre 17 y 18 festivos
        assertTrue(
                holidays.size() >= 17 && holidays.size() <= 18,
                "Colombia should have between 17 and 18 holidays"
        );
    }

    /**
     * Tests that the holiday detection method works.
     */
    @Test
    void shouldDetectHolidayCorrectly() {

        assertTrue(HolidayUtils.isHoliday(LocalDate.of(2024, 12, 25)));
        assertTrue(HolidayUtils.isHoliday(LocalDate.of(2024, 7, 20)));
        assertFalse(HolidayUtils.isHoliday(LocalDate.of(2024, 7, 21)));
    }

}