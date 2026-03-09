package Appointments.domain;


import java.time.LocalTime;
import java.util.Objects;


public class Interval {
    private LocalTime start;
    private LocalTime end;

    private Interval()
    {

    }

    public Interval(LocalTime start, LocalTime end) {

        if (start == null || end == null) {
            throw new IllegalArgumentException("Start or end null");
        }

        if (!end.equals(start) && end.isBefore(start)) {
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

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

}
