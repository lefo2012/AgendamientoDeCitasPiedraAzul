package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class IntervalListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalDate date;
    @ElementCollection
    private List<IntervalEntity> intervals;

    public void addInterval(IntervalEntity interval)
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
        for(IntervalEntity interval:intervals)
        {
            System.out.println("Begin: " + interval.getStartTime() + " - End: " + interval.getEndTime());
        }
    }
}
