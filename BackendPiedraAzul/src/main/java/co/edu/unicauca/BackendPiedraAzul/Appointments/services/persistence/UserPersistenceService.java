package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.UserEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.UserMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.repository.UserRepositoryJPA;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPersistenceService implements IUserPersistenceService {

    private final UserRepositoryJPA jpaRepository;

    private final UserMapper userMapper;

    @Autowired
    public UserPersistenceService(UserRepositoryJPA jpaRepository, UserMapper userMapper) {
        this.jpaRepository = jpaRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity saved = jpaRepository.save(entity);
        return userMapper.toDomain(saved);
    }

    @Transactional
    @Override
    public List<User> findAll() {
        java.util.List<UserEntity> entities = jpaRepository.findAll();
        return entities.stream()
                .map(userMapper::toDomain)
                .toList();
    }
}
