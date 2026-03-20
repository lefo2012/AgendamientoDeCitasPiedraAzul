package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Interval;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.IntervalList;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.IntervalEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.IntervalListEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-14T19:52:29-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class IntervalListMapperImpl implements IntervalListMapper {

    @Autowired
    private IntervalMapper intervalMapper;

    @Override
    public IntervalList toDomain(IntervalListEntity intervalListEntity) {
        if ( intervalListEntity == null ) {
            return null;
        }

        IntervalList intervalList = new IntervalList();

        intervalList.setId( intervalListEntity.getId() );
        intervalList.setDate( intervalListEntity.getDate() );
        intervalList.setIntervals( intervalEntityListToIntervalList( intervalListEntity.getIntervals() ) );

        return intervalList;
    }

    @Override
    public IntervalListEntity toEntity(IntervalList intervalList) {
        if ( intervalList == null ) {
            return null;
        }

        IntervalListEntity intervalListEntity = new IntervalListEntity();

        intervalListEntity.setId( intervalList.getId() );
        intervalListEntity.setDate( intervalList.getDate() );
        intervalListEntity.setIntervals( intervalListToIntervalEntityList( intervalList.getIntervals() ) );

        return intervalListEntity;
    }

    protected List<Interval> intervalEntityListToIntervalList(List<IntervalEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<Interval> list1 = new ArrayList<Interval>( list.size() );
        for ( IntervalEntity intervalEntity : list ) {
            list1.add( intervalMapper.toDomain( intervalEntity ) );
        }

        return list1;
    }

    protected List<IntervalEntity> intervalListToIntervalEntityList(List<Interval> list) {
        if ( list == null ) {
            return null;
        }

        List<IntervalEntity> list1 = new ArrayList<IntervalEntity>( list.size() );
        for ( Interval interval : list ) {
            list1.add( intervalMapper.toEntity( interval ) );
        }

        return list1;
    }
}
