package Appointments.services.persistence;

import Appointments.domain.Appointment;

import java.util.List;
import java.util.Optional;

public interface IAppointmentPersistenceService {
    Appointment save(Appointment appointment);
//    void update(Appointment appointment, Long id);
//    Optional<Appointment> findById(Long id);
//    List<Appointment> findAll();
//    List<Appointment> findAppointmentsByPatientID(Long id);
//    List<Appointment> findAppointmentsByDoctorID(Long id);
//    boolean deleteAppointmentById(Long id);
//    boolean deleteAll();
}
