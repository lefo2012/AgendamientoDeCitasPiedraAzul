package Appointments.entities;

import Appointments.utilities.HolidayUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.time.temporal.TemporalAdjusters;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    private Set<LocalDate> holidays = new HashSet<>();

    @ElementCollection
    private Map<LocalDate, IntervalListEntity> availableTimes;

    @ElementCollection
    private Map<LocalDate, IntervalListEntity> busyTimes;

}
