package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ConfigureScheduleDTO {
    private List<DayOfWeek> days;
    private List<IntervalListDTO> schedules;
    private int weeksRepeat;
    private int year;
}
