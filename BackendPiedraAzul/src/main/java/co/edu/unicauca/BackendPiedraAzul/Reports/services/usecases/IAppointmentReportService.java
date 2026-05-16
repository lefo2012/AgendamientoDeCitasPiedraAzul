package co.edu.unicauca.BackendPiedraAzul.Reports.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Reports.persistence.dto.AppointmentReport;

import java.time.LocalDate;
import java.util.List;

public interface IAppointmentReportService {

    List<AppointmentReport> convertInAppointmentReportDTO(List<Appointment> appointments) throws Exception;
    List<AppointmentReport> getAppointmentsReportByDateAndDoctor(Long doctorId, LocalDate date) throws Exception;
    List<AppointmentReport> getAllAppointmentsReport() throws Exception;
    String convertToCSV(Long doctorId, LocalDate date) throws Exception;

}
