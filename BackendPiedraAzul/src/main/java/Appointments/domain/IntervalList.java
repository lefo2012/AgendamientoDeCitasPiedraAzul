package Appointments.domain;

import java.util.ArrayList;
import java.util.List;

public class IntervalList {
    private List<Interval> intervals;

    public IntervalList()
    {
        intervals=new ArrayList<>();
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

    public void addInterval(Interval interval)
    {

        intervals.add(interval);
    }

    public void print()
    {
        if(!intervals.isEmpty()){
            for (Interval i:intervals)
            {
                System.out.println("Intervalo: "+ i.getStart() + " - " + i.getEnd());
            }
        }

    }
}
