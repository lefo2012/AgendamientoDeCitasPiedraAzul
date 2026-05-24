package co.edu.unicauca.BackendPiedraAzul.Appointments.controller;

import co.edu.unicauca.BackendPiedraAzul.Users.services.usecases.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private IPatientService patientService;

    @PreAuthorize("hasAnyRole('PACIENTE')")
    @GetMapping("/{patientId}/getPendingAppointments")
    public ResponseEntity<?> getPendingAppointments(@PathVariable Long patientId) throws Exception {
        return ResponseEntity.ok(patientService.getPendingAppointments(patientId));
    }

}
