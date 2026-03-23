package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Schedule;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ISchedulePersistenceService {
    @Transactional
    Schedule save(Schedule schedule);

    @Transactional
    List<Schedule> findAll();
}
