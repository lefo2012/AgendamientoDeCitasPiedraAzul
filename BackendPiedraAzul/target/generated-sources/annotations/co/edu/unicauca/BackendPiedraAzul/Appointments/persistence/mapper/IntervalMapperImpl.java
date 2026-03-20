package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Interval;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.IntervalEntity;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-14T19:52:29-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class IntervalMapperImpl implements IntervalMapper {

    @Override
    public Interval toDomain(IntervalEntity intervalEntity) {
        if ( intervalEntity == null ) {
            return null;
        }

        LocalTime startTime = null;
        LocalTime endTime = null;

        startTime = intervalEntity.getStartTime();
        endTime = intervalEntity.getEndTime();

        Interval interval = new Interval( startTime, endTime );

        return interval;
    }

    @Override
    public IntervalEntity toEntity(Interval interval) {
        if ( interval == null ) {
            return null;
        }

        IntervalEntity intervalEntity = new IntervalEntity();

        intervalEntity.setStartTime( interval.getStartTime() );
        intervalEntity.setEndTime( interval.getEndTime() );

        return intervalEntity;
    }
}
