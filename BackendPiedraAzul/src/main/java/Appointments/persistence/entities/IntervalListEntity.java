package Appointments.persistence.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Embeddable
@Getter
@Setter
public class IntervalListEntity {
    @ElementCollection
    private List<IntervalEntity> intervals;

}
