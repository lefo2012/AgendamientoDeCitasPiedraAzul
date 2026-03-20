package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.IntervalList;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Schedule;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.IntervalListEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.ScheduleEntity;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-14T19:52:29-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class ScheduleMapperImpl implements ScheduleMapper {

    @Autowired
    private IntervalListMapper intervalListMapper;

    @Override
    public Schedule toDomain(ScheduleEntity scheduleEntity) {
        if ( scheduleEntity == null ) {
            return null;
        }

        Schedule schedule = new Schedule();

        schedule.setId( scheduleEntity.getId() );
        Set<LocalDate> set = scheduleEntity.getHolidays();
        if ( set != null ) {
            schedule.setHolidays( new LinkedHashSet<LocalDate>( set ) );
        }
        schedule.setAvailableTimes( localDateIntervalListEntityMapToLocalDateIntervalListMap( scheduleEntity.getAvailableTimes() ) );
        schedule.setBusyTimes( localDateIntervalListEntityMapToLocalDateIntervalListMap( scheduleEntity.getBusyTimes() ) );

        return schedule;
    }

    @Override
    public ScheduleEntity toEntity(Schedule schedule) {
        if ( schedule == null ) {
            return null;
        }

        ScheduleEntity scheduleEntity = new ScheduleEntity();

        scheduleEntity.setId( schedule.getId() );
        Set<LocalDate> set = schedule.getHolidays();
        if ( set != null ) {
            scheduleEntity.setHolidays( new LinkedHashSet<LocalDate>( set ) );
        }
        scheduleEntity.setAvailableTimes( localDateIntervalListMapToLocalDateIntervalListEntityMap( schedule.getAvailableTimes() ) );
        scheduleEntity.setBusyTimes( localDateIntervalListMapToLocalDateIntervalListEntityMap( schedule.getBusyTimes() ) );

        return scheduleEntity;
    }

    protected Map<LocalDate, IntervalList> localDateIntervalListEntityMapToLocalDateIntervalListMap(Map<LocalDate, IntervalListEntity> map) {
        if ( map == null ) {
            return null;
        }

        Map<LocalDate, IntervalList> map1 = new LinkedHashMap<LocalDate, IntervalList>( Math.max( (int) ( map.size() / .75f ) + 1, 16 ) );

        for ( java.util.Map.Entry<LocalDate, IntervalListEntity> entry : map.entrySet() ) {
            LocalDate key = entry.getKey();
            IntervalList value = intervalListMapper.toDomain( entry.getValue() );
            map1.put( key, value );
        }

        return map1;
    }

    protected Map<LocalDate, IntervalListEntity> localDateIntervalListMapToLocalDateIntervalListEntityMap(Map<LocalDate, IntervalList> map) {
        if ( map == null ) {
            return null;
        }

        Map<LocalDate, IntervalListEntity> map1 = new LinkedHashMap<LocalDate, IntervalListEntity>( Math.max( (int) ( map.size() / .75f ) + 1, 16 ) );

        for ( java.util.Map.Entry<LocalDate, IntervalList> entry : map.entrySet() ) {
            LocalDate key = entry.getKey();
            IntervalListEntity value = intervalListMapper.toEntity( entry.getValue() );
            map1.put( key, value );
        }

        return map1;
    }
}
