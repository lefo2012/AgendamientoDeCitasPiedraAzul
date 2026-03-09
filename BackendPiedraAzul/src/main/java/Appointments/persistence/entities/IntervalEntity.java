package Appointments.persistence.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class IntervalEntity {

    private LocalTime start;
    private LocalTime end;

}
