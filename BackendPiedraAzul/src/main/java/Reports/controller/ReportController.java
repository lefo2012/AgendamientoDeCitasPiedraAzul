package Reports.controller;

import Reports.services.AppointmentReportDTO;
import Reports.services.AppointmentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rest controller for handling report-related endpoints.
 * This controller provides endpoints to retrieve appointment reports filtered by doctor and date.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private AppointmentReportService appointmentReportService;

    /**
     * obtain the report of appointments for a doctor/therapist on a specific date.
     * @param doctorId ID of the doctor/therapist
     * @param appointmentDate date of the appointments to search (format: YYYY-MM-DD)
     * @return ResponseEntity with the report data
     */
    @GetMapping("/appointments/byDoctorAndDate")
    public ResponseEntity<?> getAppointmentsByDoctorAndDate(
            @RequestParam(value = "doctorId") Long doctorId,
            @RequestParam(value = "appointmentDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate
    ) {
        return  ResponseEntity.ok("se listara");
    }
}
