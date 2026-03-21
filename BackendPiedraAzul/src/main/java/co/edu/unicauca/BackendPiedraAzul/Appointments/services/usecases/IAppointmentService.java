package co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.AppointmentDto;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ReserveAppointmentDto;

import java.util.List;

public interface IAppointmentService {

    void reserveAppointment(ReserveAppointmentDto reserveAppointmentDto) throws Exception;
    List<AppointmentDto> getScheduledAppointmentsByDoctor(Long doctorId) throws Exception;
    List<AppointmentDto> getAttendedAppointments(Long doctorId) throws Exception;
}
