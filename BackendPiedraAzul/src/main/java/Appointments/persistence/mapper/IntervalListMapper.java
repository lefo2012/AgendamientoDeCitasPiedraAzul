package Appointments.persistence.mapper;


import Appointments.domain.IntervalList;
import Appointments.persistence.entities.IntervalListEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IntervalMapper.class})
public interface IntervalListMapper {

    IntervalList toDomain(IntervalListEntity intervalListEntity);
    IntervalListEntity toEntity(IntervalList intervalList);
}
