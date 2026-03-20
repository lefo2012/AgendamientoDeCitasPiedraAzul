package co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Role;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.UserDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.RoleEntity;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.UserEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-14T19:52:29-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public User toDomain(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        User user = new User();

        user.setId( userEntity.getId() );
        user.setEmail( userEntity.getEmail() );
        user.setRoles( roleEntityListToRoleList( userEntity.getRoles() ) );
        user.setKeycloakId( userEntity.getKeycloakId() );

        return user;
    }

    @Override
    public UserEntity toEntity(User user) {
        if ( user == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( user.getId() );
        userEntity.setEmail( user.getEmail() );
        userEntity.setKeycloakId( user.getKeycloakId() );
        userEntity.setRoles( roleListToRoleEntityList( user.getRoles() ) );

        return userEntity;
    }

    @Override
    public User dtoToDomain(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setRoles( stringListToRoleList( userDTO.getRoles() ) );
        user.setEmail( userDTO.getEmail() );

        return user;
    }

    protected List<Role> roleEntityListToRoleList(List<RoleEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<Role> list1 = new ArrayList<Role>( list.size() );
        for ( RoleEntity roleEntity : list ) {
            list1.add( roleMapper.toDomain( roleEntity ) );
        }

        return list1;
    }

    protected List<RoleEntity> roleListToRoleEntityList(List<Role> list) {
        if ( list == null ) {
            return null;
        }

        List<RoleEntity> list1 = new ArrayList<RoleEntity>( list.size() );
        for ( Role role : list ) {
            list1.add( roleMapper.toEntity( role ) );
        }

        return list1;
    }

    protected List<Role> stringListToRoleList(List<String> list) {
        if ( list == null ) {
            return null;
        }

        List<Role> list1 = new ArrayList<Role>( list.size() );
        for ( String string : list ) {
            list1.add( map( string ) );
        }

        return list1;
    }
}
