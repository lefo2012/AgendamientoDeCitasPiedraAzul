package co.edu.unicauca.BackendPiedraAzul.Users.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.DoctorDTO;

import java.time.LocalDate;
import java.util.List;

public interface IDoctorService {
    Doctor register(DoctorDTO doctorDto, String client_id) throws Exception;
    List<Appointment> getAppointmentsByDoctorIDAndDate(Long doctorId, LocalDate date) throws  Exception;
}
