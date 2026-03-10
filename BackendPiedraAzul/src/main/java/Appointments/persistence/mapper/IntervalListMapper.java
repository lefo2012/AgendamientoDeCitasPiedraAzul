package Appointments.persistence.mapper;


import Appointments.domain.IntervalList;
import Appointments.persistence.entities.IntervalListEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {IntervalMapper.class})
public interface IntervalListMapper {


    IntervalList toDomain(IntervalListEntity intervalListEntity);
    IntervalListEntity toEntity(IntervalList intervalList);
}
