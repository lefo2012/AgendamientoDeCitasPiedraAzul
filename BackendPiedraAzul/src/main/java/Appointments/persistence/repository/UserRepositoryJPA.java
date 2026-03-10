package Appointments.persistence.repository;

import Appointments.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryJPA extends JpaRepository<UserEntity,Long> {
}
