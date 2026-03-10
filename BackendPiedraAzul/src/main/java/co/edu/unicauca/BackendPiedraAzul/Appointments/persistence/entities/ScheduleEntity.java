package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    @ElementCollection
    private Set<LocalDate> holidays;

    private Map<LocalDate, IntervalListEntity> availableTimes;

    private Map<LocalDate, IntervalListEntity> busyTimes;

}
