package co.edu.unicauca.BackendPiedraAzul.Appointments.controller;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.DoctorDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.PatientDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence.IDoctorPersistenceService;
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
    @Autowired
    private IDoctorPersistenceService doctorPersistenceService;

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


    @PostMapping("/createDoctor")
    public ResponseEntity<?> saveDoctor(@RequestBody DoctorDTO doctorDTO){
        try {
            Doctor doctorSaved = doctorPersistenceService.save(doctorDTO);
            return ResponseEntity.ok(doctorSaved);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error saving patient: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/getAllDoctors")
    public ResponseEntity<?> getAllDoctor(){
        try {
            List<Doctor> doctors = doctorPersistenceService.findAll();
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error retrieving patients: " + e.getMessage() + "\"}");
        }
    }
}
