package Appointments.persistence.mapper;

import Appointments.domain.Interval;
import Appointments.persistence.entities.IntervalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IntervalMapper {


    Interval toDomain(IntervalEntity intervalEntity);
    IntervalEntity toEntity(Interval interval);
}
