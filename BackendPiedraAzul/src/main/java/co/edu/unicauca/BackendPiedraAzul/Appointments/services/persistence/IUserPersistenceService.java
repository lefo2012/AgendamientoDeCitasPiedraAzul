package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.User;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IUserPersistenceService {

    @Transactional
    User save (User user);
    @Transactional
    List<User> findAll();

}
