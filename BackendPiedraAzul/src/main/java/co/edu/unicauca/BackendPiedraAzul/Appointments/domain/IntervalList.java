package co.edu.unicauca.BackendPiedraAzul.Appointments.domain;

import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.IntervalEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IntervalList {

    private Long id;

    private LocalDate date;

    private List<Interval> intervals;

    public IntervalList()
    {
        intervals=new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

    public void addInterval(Interval interval)
    {
        if(intervals.contains(interval))
        {
            throw new IllegalArgumentException("Intervals already exist");
        }
        if(intervals == null)
        {
            intervals = new ArrayList<>();

        }
        intervals.add(interval);
    }

    public void print()
    {
        for(Interval interval:intervals)
        {
            System.out.println("Begin: " + interval.getStartTime() + " - End: " + interval.getEndTime());
        }
    }
}
