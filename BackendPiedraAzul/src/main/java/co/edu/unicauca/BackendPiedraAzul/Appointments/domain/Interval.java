package co.edu.unicauca.BackendPiedraAzul.Appointments.domain;


import java.time.LocalTime;
import java.util.Objects;


public class Interval {
    private LocalTime startTime;
    private LocalTime endTime;

    private Interval()
    {

    }

    public Interval(LocalTime startTime, LocalTime endTime) {

        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start or end null");
        }

        if (!endTime.equals(startTime) && endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End must be after start");
        }

        this.startTime = startTime;
        this.endTime = endTime;
    }


    //We need to override to compare content instead of memory references, same with Date
    @Override
    public boolean equals(Object o) {
        if (o instanceof Interval) {
            Interval interval = (Interval) o;

            if (this.startTime.equals(interval.startTime) && this.endTime.equals(interval.endTime)) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    public boolean isWithin(Interval intervalToCheck) {
        return !this.startTime.isBefore(intervalToCheck.startTime) && !this.endTime.isAfter(intervalToCheck.endTime);
    }

    public boolean overlaps(Interval intervalToCheck) {
        return this.startTime.isBefore(intervalToCheck.endTime) && intervalToCheck.startTime.isBefore(this.endTime);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

}
