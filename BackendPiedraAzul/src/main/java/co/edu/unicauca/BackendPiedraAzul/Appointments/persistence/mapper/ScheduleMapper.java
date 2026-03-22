package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Schedule;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.ScheduleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IntervalListMapper.class})
public interface ScheduleMapper {

    Schedule toDomain(ScheduleEntity scheduleEntity);

    ScheduleEntity toEntity(Schedule schedule);
}
