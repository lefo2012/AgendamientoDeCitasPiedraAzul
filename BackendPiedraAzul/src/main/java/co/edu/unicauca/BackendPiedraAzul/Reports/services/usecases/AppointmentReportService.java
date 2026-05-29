package co.edu.unicauca.BackendPiedraAzul.Reports.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.AppointmentStatusEnum;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.IAppointmentService;
import co.edu.unicauca.BackendPiedraAzul.Reports.persistence.dto.AppointmentReport;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IDoctorPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.usecases.IDoctorService;
import org.springframework.stereotype.Service;
import java.nio.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling appointment report logic.
 * this class will contain methods to retrieve and process appointment data for generating reports.
 * the methods in this class will interact with the Appointments service to fetch the necessary data
 */

@Service
public class AppointmentReportService implements IAppointmentReportService{

    private final IDoctorService doctorService;
    private final IDoctorPersistenceService doctorPersistenceService;
    private final IAppointmentService appointmentService;

    public AppointmentReportService(IDoctorService doctorService,
                                    IDoctorPersistenceService doctorPersistenceService,
                                    IAppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.doctorPersistenceService = doctorPersistenceService;
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
        List<Doctor> doctors = new ArrayList<>(doctorPersistenceService.findAll());
        doctors.sort(Comparator
                .comparing((Doctor d) -> d.getFirstName() == null ? "" : d.getFirstName().trim())
                .thenComparing(d -> d.getLastName() == null ? "" : d.getLastName().trim()));

        List<Appointment> appointments = appointmentService.getAllAppointmentsByDate(date);
        Map<Long, List<Appointment>> appointmentsByDoctor = new HashMap<>();

        for (Doctor doctor : doctors) {
            appointmentsByDoctor.put(doctor.getId(), new ArrayList<>());
        }

        for (Appointment appointment : appointments) {
            Doctor doctor = appointment.getDoctor();
            Long resolvedDoctorId = doctor != null ? doctor.getId() : null;
            if (resolvedDoctorId != null && appointmentsByDoctor.containsKey(resolvedDoctorId)) {
                appointmentsByDoctor.get(resolvedDoctorId).add(appointment);
            }
        }

        int maxRows = 0;
        for (List<Appointment> doctorAppointments : appointmentsByDoctor.values()) {
            doctorAppointments.sort(Comparator.comparing(
                    a -> a.getInterval() == null ? null : a.getInterval().getStartTime(),
                    Comparator.nullsLast(Comparator.naturalOrder())
            ));
            maxRows = Math.max(maxRows, doctorAppointments.size());
        }
        if (maxRows == 0) {
            maxRows = 1;
        }

        StringBuilder csv = new StringBuilder();
        csv.append("Fecha");
        for (Doctor doctor : doctors) {
            String fullName = (doctor.getFirstName() == null ? "" : doctor.getFirstName().trim()) +
                    " " +
                    (doctor.getLastName() == null ? "" : doctor.getLastName().trim());
            csv.append(";").append(escapeCSVCell(fullName.trim()));
        }
        csv.append("\r\n");

        for (int rowIndex = 0; rowIndex < maxRows; rowIndex++) {
            csv.append(escapeCSVCell(date.toString()));
            for (Doctor doctor : doctors) {
                List<Appointment> doctorAppointments = appointmentsByDoctor.get(doctor.getId());
                if (doctorAppointments != null && rowIndex < doctorAppointments.size()) {
                    Appointment appointment = doctorAppointments.get(rowIndex);
                    String interval = "";
                    if (appointment.getInterval() != null) {
                        String start = appointment.getInterval().getStartTime() != null
                                ? appointment.getInterval().getStartTime().toString()
                                : "";
                        String end = appointment.getInterval().getEndTime() != null
                                ? appointment.getInterval().getEndTime().toString()
                                : "";
                        interval = (start + " - " + end).trim();
                    }

                    String patient = "";
                    if (appointment.getPatient() != null) {
                        String firstName = appointment.getPatient().getFirstName() != null
                                ? appointment.getPatient().getFirstName()
                                : "";
                        String lastName = appointment.getPatient().getLastName() != null
                                ? appointment.getPatient().getLastName()
                                : "";
                        patient = (firstName + " " + lastName).trim();
                    }

                    String cell = interval;
                    if (!patient.isEmpty()) {
                        cell = (cell.isEmpty() ? "" : cell + " | ") + patient;
                    }
                    csv.append(";").append(escapeCSVCell(cell.trim()));
                } else {
                    csv.append(";");
                }
            }
            csv.append("\r\n");
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

