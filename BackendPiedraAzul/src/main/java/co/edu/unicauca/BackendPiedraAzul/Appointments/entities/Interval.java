package co.edu.unicauca.BackendPiedraAzul.Appointments.entities;

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
public class Interval {

    private LocalTime start;
    private LocalTime end;

    public Interval(LocalTime start, LocalTime end) {

        if (start == null || end == null) {
            throw new IllegalArgumentException("Start or end null");
        }

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End must be after start");
        }

        this.start = start;
        this.end = end;
    }


    //We need to override to compare content instead of memory references, same with Date
    @Override
    public boolean equals(Object o) {
        if (o instanceof Interval) {
            Interval interval = (Interval) o;

            if (this.start.equals(interval.start) && this.end.equals(interval.end)) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    public boolean isWithin(Interval intervalToCheck) {
        return !this.start.isBefore(intervalToCheck.start) && !this.end.isAfter(intervalToCheck.end);
    }

    public boolean overlaps(Interval intervalToCheck) {
        return this.start.isBefore(intervalToCheck.end) && intervalToCheck.start.isBefore(this.end);
    }
}
