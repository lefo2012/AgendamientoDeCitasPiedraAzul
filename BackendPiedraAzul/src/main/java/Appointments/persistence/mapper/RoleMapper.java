package Appointments.persistence.mapper;

import org.mapstruct.Mapper;
import Appointments.domain.Role;
import Appointments.persistence.entities.RoleEntity;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toDomain(RoleEntity roleEntity);
    RoleEntity toEntity(Role role);
}
