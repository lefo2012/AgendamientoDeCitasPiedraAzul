package Appointments.persistence.mapper;

import Appointments.domain.Schedule;
import Appointments.persistence.entities.ScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {IntervalListMapper.class})
public interface ScheduleMapper {


    Schedule toDomain(ScheduleEntity scheduleEntity);
    ScheduleEntity toEntity(Schedule schedule);
}
