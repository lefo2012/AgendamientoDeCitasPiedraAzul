package co.edu.unicauca.BackendPiedraAzul.Appointments.domain;

import java.util.ArrayList;
import java.util.List;

public class IntervalList {
//
//    private Long id;

    private List<Interval> intervals;

    public IntervalList()
    {
        intervals=new ArrayList<>();
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

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
                System.out.println("Intervalo: "+ i.getStartTime() + " - " + i.getEndTime());
            }
        }

    }
}
