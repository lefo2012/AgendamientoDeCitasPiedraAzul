package Appointments.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
public class IntervalList {
    @ElementCollection
    private List<Interval> intervals;

    public IntervalList()
    {
        intervals=new ArrayList<>();
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
            System.out.println("Begin: " + interval.getStart() + " - End: " + interval.getEnd());
        }
    }
}
