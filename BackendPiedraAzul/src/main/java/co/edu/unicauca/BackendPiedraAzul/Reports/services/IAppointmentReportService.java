package co.edu.unicauca.BackendPiedraAzul.Reports.services;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Reports.Dto.AppointmentReport;
import co.edu.unicauca.BackendPiedraAzul.Users.services.usecases.IDoctorService;

import java.time.LocalDate;
import java.util.List;

public interface IAppointmentReportService {

    List<AppointmentReport> convertInAppointmentReportDTO(List<Appointment> appointments) throws Exception;
    List<AppointmentReport> getAppointmentsReport(Long doctorId, LocalDate date) throws Exception;

}
