package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import org.mapstruct.Mapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Role;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.RoleEntity;

@Mapper(componentModel = "spring")
public interface RoleMapper {


    Role toDomain(RoleEntity roleEntity);
    RoleEntity toEntity(Role role);
}
