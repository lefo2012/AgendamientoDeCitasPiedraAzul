package co.edu.unicauca.BackendPiedraAzul.Reports.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.AppointmentStatusEnum;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.IAppointmentService;
import co.edu.unicauca.BackendPiedraAzul.Reports.persistence.dto.AppointmentReport;
import co.edu.unicauca.BackendPiedraAzul.Users.services.usecases.IDoctorService;
import org.springframework.stereotype.Service;
import java.nio.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling appointment report logic.
 * this class will contain methods to retrieve and process appointment data for generating reports.
 * the methods in this class will interact with the Appointments service to fetch the necessary data
 */

@Service
public class AppointmentReportService implements IAppointmentReportService{

    private final IDoctorService doctorService;
    private final IAppointmentService appointmentService;

    public AppointmentReportService(IDoctorService doctorService,IAppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    @Override
    public List<AppointmentReport> convertInAppointmentReportDTO(List<Appointment> appointments) throws Exception{

        return appointments.stream().map(appointment -> {
            AppointmentReport reportDTO = new AppointmentReport();

            reportDTO.setPatientId(appointment.getPatient().getId());
            reportDTO.setId(appointment.getId());
            reportDTO.setDoctorName(
                    appointment.getDoctor().getFirstName() + " " +
                            appointment.getDoctor().getLastName()
            );

            reportDTO.setPatientName(
                    appointment.getPatient().getFirstName() + " " +
                            appointment.getPatient().getLastName()
            );

            reportDTO.setDate(appointment.getAppointmentDate().toString());

            reportDTO.setAppointmentInterval(appointment.getInterval().getStartTime().toString() + " - " + appointment.getInterval().getEndTime().toString() );
            return reportDTO;
        }).toList();
    }

    @Override
    public List<AppointmentReport> getAppointmentsReportByDateAndDoctor(Long doctorId, LocalDate date) throws Exception {

        List<Appointment> appointments =
                doctorService.getAppointmentsByDoctorIDAndDate(doctorId, date);

        return convertInAppointmentReportDTO(appointments);
    }

    @Override
    public List<AppointmentReport> getAppointmentsReportByDate(LocalDate date) throws Exception {
        List<Appointment> appointments = appointmentService.getAllAppointmentsByDate(date);
        return convertInAppointmentReportDTO(appointments);
    }

    @Override
    public List<AppointmentReport> getAllAppointmentsReport() throws Exception{
        List<Appointment> appointments = appointmentService.getAllAppointments();

        return convertInAppointmentReportDTO(
                appointments.stream()
                        .filter(a -> a.getAppointmentStatus() == AppointmentStatusEnum.AGENDADA)
                        .toList()
        );
    }

    @Override
    public String convertToCSV(Long doctorId, LocalDate date) throws Exception {
        StringBuilder csv = new StringBuilder();
        csv.append("Doctor;Fecha;Horario;Paciente\r\n");

        List<AppointmentReport> reports;
        if (doctorId == null){
            reports = getAppointmentsReportByDate(date);
        }else{
            reports = getAppointmentsReportByDateAndDoctor(doctorId, date);
        }

        for (AppointmentReport report : reports) {
            csv.append(escapeCSVCell(report.getDoctorName())).append(";")
                    .append(escapeCSVCell(report.getDate())).append(";")
                    .append(escapeCSVCell(report.getAppointmentInterval())).append(";")
                    .append(escapeCSVCell(report.getPatientName())).append("\r\n");
        }
        return csv.toString();

    }

    private String escapeCSVCell(String cell) {
        if (cell == null) {
            return "";
        }
        if (cell.contains("\"") || cell.contains(";") || cell.contains("\n") || cell.contains("\r")) {
            return "\"" + cell.replace("\"", "\"\"") + "\"";
        }

        return cell;
    }
}

