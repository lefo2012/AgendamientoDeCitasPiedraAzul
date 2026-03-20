package co.edu.unicauca.BackendPiedraAzul.Appointments.entities;

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

}
