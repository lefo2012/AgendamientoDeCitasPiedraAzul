package co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Role;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.RoleEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-14T19:52:29-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Role toDomain(RoleEntity roleEntity) {
        if ( roleEntity == null ) {
            return null;
        }

        Role role = new Role();

        role.setRole( roleEntity.getRole() );

        return role;
    }

    @Override
    public RoleEntity toEntity(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleEntity roleEntity = new RoleEntity();

        roleEntity.setRole( role.getRole() );

        return roleEntity;
    }
}
