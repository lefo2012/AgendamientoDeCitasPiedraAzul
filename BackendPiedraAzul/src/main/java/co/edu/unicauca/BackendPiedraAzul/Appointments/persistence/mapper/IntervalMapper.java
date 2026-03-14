package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Interval;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.IntervalEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IntervalMapper {
    Interval toDomain(IntervalEntity intervalEntity);
    IntervalEntity toEntity(Interval interval);
}
