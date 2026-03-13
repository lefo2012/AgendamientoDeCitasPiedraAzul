package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;


import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.IntervalList;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.IntervalListEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IntervalMapper.class})
public interface IntervalListMapper {


    IntervalList toDomain(IntervalListEntity intervalListEntity);
    IntervalListEntity toEntity(IntervalList intervalList);
}
