package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IAppointmentPersistenceService {

    @Transactional
    Appointment save(Appointment appointment) throws Exception;

    @Transactional
    List<Appointment> findAll() throws Exception;

    @Transactional
    Appointment findById(long id) throws Exception;

//    void update(Appointment appointment, Long id);
//    Optional<Appointment> findById(Long id);
//    List<Appointment> findAll();
//    List<Appointment> findAppointmentsByPatientID(Long id);
//    List<Appointment> findAppointmentsByDoctorID(Long id);
//    boolean deleteAppointmentById(Long id);
//    boolean deleteAll();

}
