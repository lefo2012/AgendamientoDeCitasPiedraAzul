package co.edu.unicauca.BackendPiedraAzul.Users.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IUserPersistenceService {

    @Transactional
    User save (User user);
    @Transactional
    List<User> findAll();
    @Transactional
    User findByEmail(String email);

}
