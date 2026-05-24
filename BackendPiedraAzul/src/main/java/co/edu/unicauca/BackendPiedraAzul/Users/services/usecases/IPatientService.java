package co.edu.unicauca.BackendPiedraAzul.Users.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.PatientDTO;

import java.time.LocalDate;
import java.util.List;

public interface IPatientService {
    Patient register(PatientDTO patientDto, String client_id) throws Exception;
    List<Appointment> getPendingAppointments(Long patientId) throws  Exception;
}
