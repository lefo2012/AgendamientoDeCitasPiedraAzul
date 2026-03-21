package co.edu.unicauca.BackendPiedraAzul.Users.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.DoctorDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.DoctorEntity;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper.DoctorMapper;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.repository.DoctorRepositoryJPA;
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
    public Doctor save(DoctorDTO doctorDTO) throws Exception{
        Doctor domain = doctorMapper.dtoToDomain(doctorDTO);
        DoctorEntity entity = doctorMapper.toEntity(domain);
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
