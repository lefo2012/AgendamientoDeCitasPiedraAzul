package Appointments.services.persistence;

import Appointments.domain.Appointment;
import Appointments.persistence.mapper.AppointmentMapper;
import Appointments.persistence.repository.AppointmentRepositoryJPA;
import Appointments.persistence.entities.AppointmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentPersistenceService implements IAppointmentPersistenceService {

    private final AppointmentRepositoryJPA jpaRepository;
    private final AppointmentMapper appointmentMapper;

    @Autowired
    public AppointmentPersistenceService(AppointmentRepositoryJPA appointmentRepositoryJPA, AppointmentMapper appointmentMapper) {
        this.jpaRepository = appointmentRepositoryJPA;
        this.appointmentMapper = appointmentMapper;
    }

    @Override
    public Appointment save(Appointment appointment) {
        AppointmentEntity entity = appointmentMapper.toEntity(appointment);
        AppointmentEntity saved = jpaRepository.save(entity);
        return appointmentMapper.toDomain(saved);
    }
}