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

    @Transactional
    @Override
    public Patient findById(Long id) throws Exception {
        PatientEntity patient = jpaRepository.findById(id)
                . orElseThrow(() -> new Exception("Patient not found with id: " + id));

        return patientMapper.toDomain(patient);

    }

    @Transactional
    @Override
    public Patient findByIdentificationNumber(String identificationNumber) throws Exception {
        PatientEntity patient = jpaRepository.findByIdentificationNumber(identificationNumber)
                .orElseThrow(() -> new Exception("Patient not found with identification number: " + identificationNumber));

        return patientMapper.toDomain(patient);
    }

    @Transactional
    @Override
    public Patient findByKeycloakId(String keycloakId) throws Exception{
        PatientEntity patient = jpaRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new Exception("Patient not found with keycloakId: " + keycloakId));
        return patientMapper.toDomain(patient);
    }

    @Transactional
    @Override
    public List<Patient> findByIdentificationNumberContaining(String identificationNumber) throws Exception {
        List<PatientEntity> patients = jpaRepository.findByIdentificationNumberContaining(identificationNumber)
                .orElseThrow(() -> new Exception("No patients found with identification number containing: " + identificationNumber));
        return patients.stream()
                .map(patientMapper::toDomain)
                .toList();
    }
}
