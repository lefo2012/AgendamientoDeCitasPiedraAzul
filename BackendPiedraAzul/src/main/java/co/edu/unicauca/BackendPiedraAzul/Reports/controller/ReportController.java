package co.edu.unicauca.BackendPiedraAzul.Reports.controller;

import co.edu.unicauca.BackendPiedraAzul.Reports.persistence.dto.AppointmentReport;
import co.edu.unicauca.BackendPiedraAzul.Reports.services.usecases.IAppointmentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
/**
 * Rest controller for handling report-related endpoints.
 * This controller provides endpoints to retrieve appointment reports filtered by doctor and date.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private IAppointmentReportService appointmentReportService;

    @GetMapping("/appointmentsByDoctorAndDate")
    @PreAuthorize("hasAnyRole('MEDICO','ADMIN')")
    public ResponseEntity<?> getAppointmentsByDoctorAndDate(
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("appointmentDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate
    ) {
        try {

            List<AppointmentReport> report =
                    appointmentReportService.getAppointmentsReportByDateAndDoctor(doctorId, appointmentDate);

            return ResponseEntity.ok(report);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error getting report for doctor with id: "
                            + doctorId + " " + e.getMessage() + "\"}");
        }
    }


    @PreAuthorize("hasAnyRole('MEDICO','ADMIN')")
    @GetMapping("/appointmentsReport")
    public ResponseEntity<?> getAppointmentsReport() {
        try {
            List<AppointmentReport> report =
                    appointmentReportService.getAllAppointmentsReport();

            return ResponseEntity.ok(report);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error getting appointments" + e.getMessage() + "\"}");
        }
    }

    @PreAuthorize("hasAnyRole('MEDICO','ADMIN')")
    @GetMapping("/exportCSVAppointments")
    public ResponseEntity<?> getAppointmentsReportByDoctorAndDate(
            @RequestParam(value = "doctorId", required = false) Long doctorId,
            @RequestParam("appointmentDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate
    ) {
        try {
            String csv = appointmentReportService.convertToCSV(doctorId, appointmentDate);

            byte[] csvBytes = csv.getBytes(StandardCharsets.UTF_8);
            byte[] bom = new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(bom);
            outputStream.write(csvBytes);
            byte[] responseBytes = outputStream.toByteArray();

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=ListaCitas"+appointmentDate+".csv")
                    .header("Content-Type", "text/csv; charset=UTF-8")
                    .body(responseBytes);
        }catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error exporting report for doctor with id: "
                            + doctorId + " and date: " +appointmentDate + " " + e.getMessage() + "\"}");

        }
    }
}
