package co.edu.unicauca.BackendPiedraAzul.Appointments.controller;

import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ReserveAppointmentDto;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentsController {
    @Autowired
    private IAppointmentService appointmentService;
    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestBody ReserveAppointmentDto dto) {
        try {
            appointmentService.reserveAppointment(dto);

            return  ResponseEntity.ok("Reserva realizada con exito");

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Impossibilidad al reservar la cita"+ e.getMessage());
        }
    }

}
