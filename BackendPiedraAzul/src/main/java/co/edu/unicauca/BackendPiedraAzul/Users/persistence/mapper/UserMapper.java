package co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Role;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.RoleEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.UserDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    User toDomain(UserEntity userEntity);
    UserEntity toEntity(User user);

    @Mapping(source = "roles", target = "roles")
    User dtoToDomain(UserDTO userDTO);

    default Role map(String role) {
        Role r = new Role();
        r.setRole(RoleEnum.valueOf(role));
        return r;
    }
}
