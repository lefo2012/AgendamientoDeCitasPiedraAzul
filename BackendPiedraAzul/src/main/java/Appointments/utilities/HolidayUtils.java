package Appointments.utilities;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class HolidayUtils {

    /**
     * Generates a list of holiday dates in Colombia for a given year.
     * Includes fixed and movable holidays calculated automatically.
     *
     * @param year The year for which to generate the holidays.
     * @return List of LocalDate with the holiday dates.
     */
    public static List<LocalDate> generateColombianHolidays(int year) {
        List<LocalDate> holidays = new ArrayList<>();

        // Fixed holidays
        holidays.add(LocalDate.of(year, 1, 1));   // New Year's Day
        holidays.add(LocalDate.of(year, 5, 1));   // Labor Day
        holidays.add(LocalDate.of(year, 7, 20));  // Independence Day
        holidays.add(LocalDate.of(year, 8, 7));   // Battle of Boyacá
        holidays.add(LocalDate.of(year, 8, 15));  // Assumption of the Virgin
        holidays.add(LocalDate.of(year, 10, 12)); // Columbus Day
        holidays.add(LocalDate.of(year, 11, 1));  // All Saints' Day
        holidays.add(LocalDate.of(year, 11, 11)); // Independence of Cartagena
        holidays.add(LocalDate.of(year, 12, 8));  // Immaculate Conception
        holidays.add(LocalDate.of(year, 12, 25)); // Christmas

        // Movable holidays

        // Epiphany: January 6, if not Monday, moved to the following Monday
        LocalDate reyes = LocalDate.of(year, 1, 6);
        if (reyes.getDayOfWeek() != DayOfWeek.MONDAY) {
            reyes = reyes.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }
        holidays.add(reyes);

        // Saint Peter and Saint Paul: June 29, if not Monday, moved to the following Monday
        LocalDate sanPedro = LocalDate.of(year, 6, 29);
        if (sanPedro.getDayOfWeek() != DayOfWeek.MONDAY) {
            sanPedro = sanPedro.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }
        holidays.add(sanPedro);

        // Holy Week: Calculate Easter Sunday using Meeus/Jones/Butcher algorithm
        LocalDate easter = calculateEasterSunday(year);
        holidays.add(easter.minusDays(3)); // Maundy Thursday
        holidays.add(easter.minusDays(2)); // Good Friday
        holidays.add(easter);              // Easter Sunday
        holidays.add(easter.plusDays(39)); // Ascension Thursday (40 days after, but it's Thursday)
        holidays.add(easter.plusDays(60)); // Corpus Christi
        holidays.add(easter.plusDays(68)); // Sacred Heart

        return holidays;
    }

    /**
     * Calculates the date of Easter Sunday for a given year using the Meeus/Jones/Butcher algorithm.
     *
     * @param year The year.
     * @return LocalDate of Easter Sunday.
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
