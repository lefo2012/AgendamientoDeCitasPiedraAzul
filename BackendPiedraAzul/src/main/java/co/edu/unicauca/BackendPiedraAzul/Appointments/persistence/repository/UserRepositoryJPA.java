package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.repository;

import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryJPA extends JpaRepository<UserEntity,Long> {
}
