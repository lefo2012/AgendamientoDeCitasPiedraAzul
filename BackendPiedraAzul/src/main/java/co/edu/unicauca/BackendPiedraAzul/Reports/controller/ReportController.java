package co.edu.unicauca.BackendPiedraAzul.Reports.controller;

import co.edu.unicauca.BackendPiedraAzul.Reports.persistence.dto.AppointmentReport;
import co.edu.unicauca.BackendPiedraAzul.Reports.services.usecases.IAppointmentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
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

    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
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


}
