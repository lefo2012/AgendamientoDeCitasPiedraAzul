package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Schedule;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.ScheduleEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.ScheduleMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.repository.ScheduleRepositoryJPA;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulePersistenceService implements ISchedulePersistenceService {

    private final ScheduleRepositoryJPA jpaRepository;

    private final ScheduleMapper scheduleMapper;

    @Autowired
    public SchedulePersistenceService(ScheduleRepositoryJPA jpaRepository, ScheduleMapper scheduleMapper) {
        this.jpaRepository = jpaRepository;
        this.scheduleMapper = scheduleMapper;
    }

    @Transactional
    @Override
    public Schedule save(Schedule schedule) {
        ScheduleEntity entity = scheduleMapper.toEntity(schedule);
        ScheduleEntity saved = jpaRepository.save(entity);
        return scheduleMapper.toDomain(saved);
    }

    @Transactional
    @Override
    public List<Schedule> findAll() {
        List<ScheduleEntity> entities = jpaRepository.findAll();
        return entities.stream()
                .map(scheduleMapper::toDomain)
                .toList();
    }
}
