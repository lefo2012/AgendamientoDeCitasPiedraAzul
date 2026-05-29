package co.edu.unicauca.BackendPiedraAzul.Users.persistence.repository;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.SpecialtyEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepositoryJPA extends JpaRepository<DoctorEntity,Long> {

    List<DoctorEntity> findBySpecialtiesContaining(SpecialtyEnum specialty);

    @Query("SELECT d FROM DoctorEntity d WHERE d.user.email = :email")
    Optional <DoctorEntity> findByEmail(String email) throws Exception;

    @Query("SELECT d FROM DoctorEntity d WHERE d.user.keycloakId = :keycloakId")
    Optional<DoctorEntity> findByKeycloakId(String keycloakId)  throws Exception;

}
