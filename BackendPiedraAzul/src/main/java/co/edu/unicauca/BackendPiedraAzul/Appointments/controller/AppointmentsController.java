package co.edu.unicauca.BackendPiedraAzul.Appointments.controller;

import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ReserveAppointmentDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence.IAppointmentPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentsController {

    @Autowired
    private IAppointmentPersistenceService appointmentPersistenceService;

    @Autowired
    private IAppointmentService appointmentService;

    @PreAuthorize("hasAnyRole('MEDICO','PACIENTE')")
    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestBody ReserveAppointmentDTO dto) {
        try {
            appointmentService.reserveAppointment(dto);

            // Retornamos un objeto JSON
            Map<String, String> response = new HashMap<>();
            response.put("message", "Reserva realizada con éxito");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Imposibilidad al reservar la cita: " + e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/getAppointments")
    @PreAuthorize("hasAnyRole('MEDICO','PACIENTE')")
    public ResponseEntity<?> getAppointments() {
        try {

            return  ResponseEntity.ok(appointmentPersistenceService.findAll());
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body("No se encontraron citas: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentPersistenceService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al obtener la cita con id: " + id + " " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('MEDICO','PACIENTE')")
    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id) {
        try {
            appointmentService.cancelAppointment(id);
            return ResponseEntity.ok("Cita cancelada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al cancelar: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('MEDICO')")
    @PostMapping("/reschedule")
    public ResponseEntity<?> reSchedule(@RequestBody ReserveAppointmentDTO dto) {
        try {
            appointmentService.reSchedule(dto);

            // Retornamos un objeto JSON
            Map<String, String> response = new HashMap<>();
            response.put("message", "Reserva realizada con éxito");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Imposibilidad al reservar la cita: " + e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


}
