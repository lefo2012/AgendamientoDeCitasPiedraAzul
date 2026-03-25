package co.edu.unicauca.BackendPiedraAzul.Appointments.controller;

import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ReserveAppointmentDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence.IAppointmentPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAppointments() {
        try {

            return  ResponseEntity.ok(appointmentPersistenceService.findAll());
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body("no existe");
        }
    }

}
