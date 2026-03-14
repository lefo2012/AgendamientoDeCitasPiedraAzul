package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.MedicalHistory;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.MedicalHistoryEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.MedicalHistoryMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.repository.MedicalHistoryRepositoryJPA;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalHistoryPersistenceService implements IMedicalHistoryPersistenceService {

    private final MedicalHistoryRepositoryJPA jpaRepository;

    private final MedicalHistoryMapper medicalHistoryMapper;

    @Autowired
    public MedicalHistoryPersistenceService(MedicalHistoryRepositoryJPA jpaRepository, MedicalHistoryMapper medicalHistoryMapper) {
        this.jpaRepository = jpaRepository;
        this.medicalHistoryMapper = medicalHistoryMapper;
    }

    @Transactional
    @Override
    public MedicalHistory save(MedicalHistory medicalHistory) {
        MedicalHistoryEntity entity = medicalHistoryMapper.toEntity(medicalHistory);
        MedicalHistoryEntity saved = jpaRepository.save(entity);
        return medicalHistoryMapper.toDomain(saved);
    }

    @Transactional
    @Override
    public java.util.List<MedicalHistory> findAll() {
        java.util.List<MedicalHistoryEntity> entities = jpaRepository.findAll();
        return entities.stream()
                .map(medicalHistoryMapper::toDomain)
                .toList();
    }

}
