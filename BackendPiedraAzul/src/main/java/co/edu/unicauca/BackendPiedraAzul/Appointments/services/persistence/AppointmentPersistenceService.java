package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.AppointmentMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.repository.AppointmentRepositoryJPA;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.AppointmentEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentPersistenceService implements IAppointmentPersistenceService {

    @Autowired
    private AppointmentRepositoryJPA jpaRepository;
    @Autowired
    private AppointmentMapper appointmentMapper;

    @Transactional
    @Override
    public Appointment save(Appointment appointment) throws Exception {
        AppointmentEntity entity = appointmentMapper.toEntity(appointment);
        AppointmentEntity saved = jpaRepository.save(entity);
        return appointmentMapper.toDomain(saved);
    }
    //Delete annotation transactional because the method is not a transaction.
    @Override
    public List<Appointment> findAll() throws Exception{
        List<AppointmentEntity> entities = jpaRepository.findAll();
        List<Appointment> appointments = new ArrayList<>();

        for (AppointmentEntity entity : entities) {
            appointments.add(appointmentMapper.toDomain(entity));
        }

        return appointments;
    }

}