package co.edu.unicauca.BackendPiedraAzul.Users.services.persistence;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.PatientDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.PatientEntity;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper.PatientMapper;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.repository.PatientRepositoryJPA;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientPersistenceService implements IPatientPersistenceService {

    private final PatientRepositoryJPA jpaRepository;

    private final PatientMapper patientMapper;

    @Autowired
    public PatientPersistenceService (PatientRepositoryJPA jpaRepository, PatientMapper patientMapper) {
        this.jpaRepository = jpaRepository;
        this.patientMapper = patientMapper;
    }

    @Transactional
    @Override
    public Patient save(Patient patient) throws Exception{
        PatientEntity entity = patientMapper.toEntity(patient);
        PatientEntity saved = jpaRepository.save(entity);
        return patientMapper.toDomain(saved);
    }

    @Transactional
    @Override
    public Patient save(PatientDTO patient) throws Exception{
        Patient domain = patientMapper.dtoToDomain(patient);
        PatientEntity entity = patientMapper.toEntity(domain);
        PatientEntity saved = jpaRepository.save(entity);

        return patientMapper.toDomain(saved);
    }

    @Transactional
    @Override
    public List<Patient> findAll() throws Exception{
        List<PatientEntity> entities = jpaRepository.findAll();
        return entities.stream()
                .map(patientMapper::toDomain)
                .toList();
    }
}
