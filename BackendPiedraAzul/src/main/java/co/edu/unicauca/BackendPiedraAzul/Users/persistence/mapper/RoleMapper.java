package co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper;

import org.mapstruct.Mapper;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Role;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.RoleEntity;

@Mapper(componentModel = "spring")
public interface RoleMapper {


    Role toDomain(RoleEntity roleEntity);
    RoleEntity toEntity(Role role);
}
