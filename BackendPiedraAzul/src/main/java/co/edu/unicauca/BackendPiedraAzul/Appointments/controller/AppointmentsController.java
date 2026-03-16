package co.edu.unicauca.BackendPiedraAzul.Appointments.controller;

import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.AppointmentDto;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ConfigureScheduleDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.AppointmentService;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentsController {
    @Autowired
    AppointmentService appointmentService;
    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestBody AppointmentDto dto) {
        try {
            appointmentService.reserveApointment(dto);

            return  ResponseEntity.ok("Reserva realizada con exito");

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Impossibilidad al reservar la cita"+ e.getMessage());
        }
    }

}
