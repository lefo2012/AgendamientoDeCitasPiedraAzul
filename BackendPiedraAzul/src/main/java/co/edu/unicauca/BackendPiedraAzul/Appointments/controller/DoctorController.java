package co.edu.unicauca.BackendPiedraAzul.Appointments.controller;

import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ConfigureScheduleDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.IAppointmentService;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    @Autowired
    private IScheduleService scheduleService;
    @Autowired
    private IAppointmentService appointmentService;

    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PostMapping("/{doctorId}/configureSchedule")
    public ResponseEntity<?> configureSchedule(
            @PathVariable Long doctorId,
            @RequestBody ConfigureScheduleDTO dto) {
        try {
            boolean resultado = scheduleService.configureSchedule(doctorId, dto);

            return ResponseEntity.ok("Agenda configurada: "+ resultado);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error configuring the schedule for the doctor with id: "+ doctorId +" " + e.getMessage() + "\"}");
        }
    }

    @PreAuthorize("hasAnyRole('DOCTOR')")
    @GetMapping("/{doctorId}/getScheduledAppointments")
    public ResponseEntity<?> getScheduledAppointments(@PathVariable Long doctorId) throws Exception {

        return ResponseEntity.ok(appointmentService.getScheduledAppointmentsByDoctor(doctorId));
    }

    @PreAuthorize("hasAnyRole('DOCTOR')")
    @GetMapping("/{doctorId}/getAttendedAppointments")
    public ResponseEntity<?> getAttendedAppointments(@PathVariable Long doctorId) throws Exception {
        return ResponseEntity.ok(appointmentService.getAttendedAppointments(doctorId));
    }

}
