package co.edu.unicauca.BackendPiedraAzul.Reports.services;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Reports.Dto.AppointmentReport;
import co.edu.unicauca.BackendPiedraAzul.Users.services.usecases.IDoctorService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for handling appointment report logic.
 * this class will contain methods to retrieve and process appointment data for generating reports.
 * the methods in this class will interact with the Appointments service to fetch the necessary data
 */

@Service
public class AppointmentReportService implements IAppointmentReportService{

    private final IDoctorService doctorService;

    public AppointmentReportService(IDoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Override
    public List<AppointmentReport> convertInAppointmentReportDTO(List<Appointment> appointments) throws Exception{

        return appointments.stream().map(appointment -> {
            AppointmentReport reportDTO = new AppointmentReport();

            reportDTO.setDoctorName(
                    appointment.getDoctor().getFirstName() + " " +
                            appointment.getDoctor().getLastName()
            );

            reportDTO.setPatientName(
                    appointment.getPatient().getFirstName() + " " +
                            appointment.getPatient().getLastName()
            );

            reportDTO.setAppointmentInterval(appointment.getInterval().getStartTime().toString() + " - " + appointment.getInterval().getEndTime().toString() );
            return reportDTO;
        }).toList();
    }

    @Override
    public List<AppointmentReport> getAppointmentsReport(Long doctorId, LocalDate date) throws Exception {

        List<Appointment> appointments =
                doctorService.getAppointmentsByDoctorIDAndDate(doctorId, date);

        return convertInAppointmentReportDTO(appointments);
    }

}

