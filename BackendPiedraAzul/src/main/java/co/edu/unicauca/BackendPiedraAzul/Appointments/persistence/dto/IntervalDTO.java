package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IntervalDTO {
    private String startTime;
    private String endTime;
}
