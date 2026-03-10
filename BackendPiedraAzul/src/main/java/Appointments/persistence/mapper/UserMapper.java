package Appointments.persistence.mapper;

import Appointments.domain.User;
import Appointments.persistence.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {


    User toDomain(UserEntity userEntity);
    UserEntity toEntity(User user);
}
