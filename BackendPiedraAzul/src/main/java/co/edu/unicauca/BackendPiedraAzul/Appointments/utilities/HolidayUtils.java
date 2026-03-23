package co.edu.unicauca.BackendPiedraAzul.Appointments.utilities;

import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for handling Colombian holidays.
 * Implements the Emiliani Law which moves certain holidays to Mondays.
 * Automatically calculates Easter-based holidays using the Meeus/Jones/Butcher algorithm.
 * Uses a thread-safe cache to improve performance across multiple years.
 */
@NoArgsConstructor
public class HolidayUtils {

    // Cache of holidays by year (thread-safe)
    private static final Map<Integer, Set<LocalDate>> CACHE = new ConcurrentHashMap<>();

    /**
     * Gets all Colombian holidays for a given year.
     */
    public static Set<LocalDate> getHolidays(int year) {
        return CACHE.computeIfAbsent(year, HolidayUtils::calculateHolidays);
    }

    /**
     * Checks if a given date is a Colombian holiday.
     */
    public static boolean isHoliday(LocalDate date) {
        return getHolidays(date.getYear()).contains(date);
    }

    /**
     * Calculates all Colombian holidays for a given year.
     */
    private static Set<LocalDate> calculateHolidays(int year) {

        Set<LocalDate> holidays = new HashSet<>();

        // Fixed holidays (do not move)
        holidays.add(LocalDate.of(year, 1, 1));
        holidays.add(LocalDate.of(year, 5, 1));
        holidays.add(LocalDate.of(year, 7, 20));
        holidays.add(LocalDate.of(year, 8, 7));
        holidays.add(LocalDate.of(year, 12, 8));
        holidays.add(LocalDate.of(year, 12, 25));

        // Emiliani Law (moved to Monday)
        holidays.add(moveToMonday(LocalDate.of(year, 1, 6)));   // Epiphany
        holidays.add(moveToMonday(LocalDate.of(year, 3, 19)));  // Saint Joseph
        holidays.add(moveToMonday(LocalDate.of(year, 6, 29)));  // Saint Peter and Paul
        holidays.add(moveToMonday(LocalDate.of(year, 8, 15))); // Assumption of the Virgin
        holidays.add(moveToMonday(LocalDate.of(year, 10, 12))); // Columbus Day
        holidays.add(moveToMonday(LocalDate.of(year, 11, 1)));  // All Saints' Day
        holidays.add(moveToMonday(LocalDate.of(year, 11, 11))); // Independence of Cartagena

        // Easter calculation
        LocalDate easter = calculateEasterSunday(year);

        // Holy Week
        holidays.add(easter.minusDays(3)); // Maundy Thursday
        holidays.add(easter.minusDays(2)); // Good Friday

        // Religious holidays moved to Monday
        holidays.add(moveToMonday(easter.plusDays(43))); // Ascension
        holidays.add(moveToMonday(easter.plusDays(64))); // Corpus Christi
        holidays.add(moveToMonday(easter.plusDays(71))); // Sacred Heart

        return Collections.unmodifiableSet(holidays);
    }

    /**
     * Moves the given date to the next Monday or same Monday if already Monday.
     */
    private static LocalDate moveToMonday(LocalDate date) {
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
    }

    /**
     * Calculates Easter Sunday using the Meeus/Jones/Butcher algorithm.
     */
    private static LocalDate calculateEasterSunday(int year) {

        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;

        int month = (h + l - 7 * m + 114) / 31;
        int day = ((h + l - 7 * m + 114) % 31) + 1;

        return LocalDate.of(year, month, day);
    }
}
