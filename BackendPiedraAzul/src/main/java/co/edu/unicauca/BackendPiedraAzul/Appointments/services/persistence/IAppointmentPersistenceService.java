package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;

public interface IAppointmentPersistenceService {
    Appointment save(Appointment appointment) throws Exception;
//    void update(Appointment appointment, Long id);
//    Optional<Appointment> findById(Long id);
//    List<Appointment> findAll();
//    List<Appointment> findAppointmentsByPatientID(Long id);
//    List<Appointment> findAppointmentsByDoctorID(Long id);
//    boolean deleteAppointmentById(Long id);
//    boolean deleteAll();
}
