package co.edu.unicauca.BackendPiedraAzul.Appointments.controller;

import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ReserveAppointmentDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence.IAppointmentPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

            return  ResponseEntity.ok("Reserva realizada con exito");

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Impossibilidad al reservar la cita"+ e.getMessage());
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
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentPersistenceService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al obtener la cita con id: " + id + " " + e.getMessage());
        }
    }

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

}
