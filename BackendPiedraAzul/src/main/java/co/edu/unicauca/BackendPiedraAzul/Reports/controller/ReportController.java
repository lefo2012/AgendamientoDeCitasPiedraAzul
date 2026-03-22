package co.edu.unicauca.BackendPiedraAzul.Reports.controller;

import co.edu.unicauca.BackendPiedraAzul.Reports.services.AppointmentReportService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.usecases.DoctorService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.usecases.IDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Rest controller for handling report-related endpoints.
 * This controller provides endpoints to retrieve appointment reports filtered by doctor and date.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private AppointmentReportService appointmentReportService;

    @Autowired
    private IDoctorService doctorService;

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
