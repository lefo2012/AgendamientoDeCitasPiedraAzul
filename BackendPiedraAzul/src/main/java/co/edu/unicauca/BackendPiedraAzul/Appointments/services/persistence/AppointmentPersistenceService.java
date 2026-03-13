package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.AppointmentMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.repository.AppointmentRepositoryJPA;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.AppointmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentPersistenceService implements IAppointmentPersistenceService {
    @Autowired
    private AppointmentRepositoryJPA jpaRepository;
    @Autowired
    private AppointmentMapper appointmentMapper;


    @Override
    public Appointment save(Appointment appointment) throws Exception {
        AppointmentEntity entity = appointmentMapper.toEntity(appointment);
        AppointmentEntity saved = jpaRepository.save(entity);
        return appointmentMapper.toDomain(saved);
    }
}