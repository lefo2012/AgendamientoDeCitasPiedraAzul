package Appointments.persistence.mapper;

import Appointments.domain.Schedule;
import Appointments.persistence.entities.ScheduleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IntervalListMapper.class})
public interface ScheduleMapper {
    Schedule toDomain(ScheduleEntity scheduleEntity);
    ScheduleEntity toEntity(Schedule schedule);
}
