package co.edu.unicauca.BackendPiedraAzul.Appointments.controller;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.PatientDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence.IPatientPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/appointments")
public class AppointmentController{

    @Autowired
    private IPatientPersistenceService patientPersistenceService;

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    @PostMapping("/createPatient")
    public ResponseEntity<?> savePatient(@RequestBody PatientDTO patientDTO){
        try {
            Patient patientSaved = patientPersistenceService.save(patientDTO);
            return ResponseEntity.ok(patientSaved);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error saving patient: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/getAllPatients")
    public ResponseEntity<?> getAllPatients(){
        try {
            List<Patient> patients = patientPersistenceService.findAll();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error retrieving patients: " + e.getMessage() + "\"}");
        }
    }
}
