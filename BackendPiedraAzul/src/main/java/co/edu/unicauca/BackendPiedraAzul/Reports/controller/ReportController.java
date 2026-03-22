package co.edu.unicauca.BackendPiedraAzul.Reports.controller;

import co.edu.unicauca.BackendPiedraAzul.Reports.Dto.AppointmentReport;
import co.edu.unicauca.BackendPiedraAzul.Reports.services.AppointmentReportService;
import co.edu.unicauca.BackendPiedraAzul.Reports.services.IAppointmentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAppointmentsByDoctorAndDate(
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("appointmentDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate
    ) {
        try {

            List<AppointmentReport> report =
                    appointmentReportService.getAppointmentsReport(doctorId, appointmentDate);

            return ResponseEntity.ok(report);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error getting report for doctor with id: "
                            + doctorId + " " + e.getMessage() + "\"}");
        }
    }
}
