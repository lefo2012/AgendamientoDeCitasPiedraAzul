package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class IntervalEntity {
    private LocalTime startTime;
    private LocalTime endTime;
}

