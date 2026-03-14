package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.DoctorEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.DoctorMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.repository.DoctorRepositoryJPA;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorPersistenceService implements IDoctorPersistenceService {

    private final DoctorRepositoryJPA jpaRepository;

    private final DoctorMapper doctorMapper;

    @Autowired
    public DoctorPersistenceService (DoctorRepositoryJPA jpaRepository, DoctorMapper doctorMapper) {
        this.jpaRepository = jpaRepository;
        this.doctorMapper = doctorMapper;
    }

    @Transactional
    @Override
    public Doctor save(Doctor doctor) throws Exception{
        DoctorEntity entity = doctorMapper.toEntity(doctor);
        DoctorEntity saved = jpaRepository.save(entity);
        return doctorMapper.toDomain(saved);
    }

    @Transactional
    @Override
    public List<Doctor> findAll() throws Exception{
        List<DoctorEntity> entities = jpaRepository.findAll();
        return entities.stream()
                .map(doctorMapper::toDomain)
                .toList();
    }
}
