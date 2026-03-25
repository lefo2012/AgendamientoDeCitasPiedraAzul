package co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.AppointmentDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ReserveAppointmentDTO;

import java.util.List;

public interface IAppointmentService {

    void reserveAppointment(ReserveAppointmentDTO reserveAppointmentDto) throws Exception;
    List<AppointmentDTO> getScheduledAppointmentsByDoctor(Long doctorId) throws Exception;
    List<AppointmentDTO> getAttendedAppointments(Long doctorId) throws Exception;
    List<Appointment> getAllAppointments()throws Exception;
    void cancelAppointment(Long appointmentId) throws Exception;
}
