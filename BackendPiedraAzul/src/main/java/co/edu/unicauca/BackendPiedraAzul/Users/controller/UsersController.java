package co.edu.unicauca.BackendPiedraAzul.Users.controller;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.SpecialtyEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.ConfDoctorDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.DoctorDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.PatientDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper.DoctorMapper;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IDoctorPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IPatientPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.usecases.IDoctorService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.usecases.IPatientService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.usecases.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private IPatientPersistenceService patientPersistenceService;
    @Autowired
    private IDoctorPersistenceService doctorPersistenceService;
    @Autowired
    private IDoctorService doctorService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IPatientService patientService;

    private static final String CLIENT_ID = "piedraAzul-app";

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    @PostMapping("/registerPatient")
    public ResponseEntity<?> registerPatient(@RequestBody PatientDTO patientDTO){
        try {
            Patient patientSaved = patientService.register(patientDTO, CLIENT_ID);
            return ResponseEntity.ok(patientSaved);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error registering patient: " + e.getMessage() + "\"}");
        }
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
    @PreAuthorize("hasAnyRole('MEDICO','PACIENTE')")
    public ResponseEntity<?> getAllPatients(){
        try {
            List<Patient> patients = patientPersistenceService.findAll();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error retrieving patients: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/registerDoctor")
    @PreAuthorize("hasAnyRole('MEDICO','ADMIN')")
    public ResponseEntity<?> registerDoctor(@RequestBody DoctorDTO doctorDTO){
        try {
            Doctor doctorSaved = doctorService.register(doctorDTO, CLIENT_ID);
            return ResponseEntity.ok(doctorSaved);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error registering doctor: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/createDoctor")
    @PreAuthorize("hasAnyRole('MEDICO','ADMIN')")
    public ResponseEntity<?> saveDoctor(@RequestBody DoctorDTO doctorDTO){
        try {
            Doctor doctorSaved = doctorPersistenceService.save(doctorDTO);
            return ResponseEntity.ok(doctorSaved);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error creating doctor: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/updateDoctor")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorDTO doctorDTO){
        try {
            Doctor doctorUpdate = doctorPersistenceService.update(doctorDTO);
            return ResponseEntity.ok(doctorUpdate);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error updating doctor: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/updatePatient")
    @PreAuthorize("hasAnyRole('PACIENTE')")
    public ResponseEntity<?> updatePatient(@RequestBody PatientDTO patientDTO){
        try {
            Patient patientUpdate = patientPersistenceService.update(patientDTO);
            return ResponseEntity.ok(patientUpdate);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error updating patient: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/getAllDoctors")
    @PreAuthorize("hasAnyRole('MEDICO','ADMIN','PACIENTE')")
    public ResponseEntity<?> getAllDoctor(){
        try {
            List<Doctor> doctors = doctorPersistenceService.findAll();
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error retrieving doctors: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/getAllConfigDoctors")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getAllConfigDoctors(){
        try {
            List<ConfDoctorDTO> doctors = doctorService.getAllConfigDoctors();
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error retrieving doctors: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/getDoctorsBySpecialty/{specialty}")
    @PreAuthorize("hasAnyRole('MEDICO','ADMIN','PACIENTE')")
    public ResponseEntity<?> getDoctorBySpecialty(@PathVariable String specialty){
        try {
            List<Doctor> doctors = doctorPersistenceService.findBySpecialtiesContaining(SpecialtyEnum.valueOf(specialty));
            return ResponseEntity.ok(doctors);
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error retrieving doctors: " + e.getMessage() + "\"}");
        }

    }

    @GetMapping("/getPatientByIdentificationNumber/{identificationNumber}")
    @PreAuthorize("hasAnyRole('MEDICO','ADMIN')")
    public ResponseEntity<?> getPatientByIdentificationNumber(@PathVariable String identificationNumber) {
        try {
            Patient patient = patientPersistenceService.findByIdentificationNumber(identificationNumber);
            if (patient != null) {
                return ResponseEntity.ok(patient);
            } else {
                return ResponseEntity.status(404)
                        .body("{\"error\":\"Patient not found with identification number: " + identificationNumber + "\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error retrieving patient: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/getPatientById/{id}")
    @PreAuthorize("hasAnyRole('PACIENTE')")
    public ResponseEntity<?> getPatientById(@PathVariable String id) {
        try {
            Patient patient = patientPersistenceService.findById(Long.valueOf(id));
            if (patient != null) {
                return ResponseEntity.ok(patient);
            } else {
                return ResponseEntity.status(404)
                        .body("{\"error\":\"Patient not found  with id: " + id + "\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error retrieving patient: " + e.getMessage() + "\"}");
        }
    }


    @GetMapping("/getDoctorById/{id}")
    @PreAuthorize("hasAnyRole('MEDICO','ADMIN', 'PACIENTE')")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        try {
            Doctor doctor = doctorPersistenceService.findById(id);
            if (doctor != null) {
                return ResponseEntity.ok(doctor);
            } else {
                return ResponseEntity.status(404)
                        .body("{\"error\":\"Doctor not found with id: " + id + "\"}");
            }
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error retrieving doctor: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/searchPatientsByIdentificationNumber/{identificationNumber}")
    @PreAuthorize("hasAnyRole('MEDICO','ADMIN','PACIENTE')")
    public ResponseEntity<?> searchPatientsByIdentificationNumber(
            @PathVariable String identificationNumber) {
        try {
            List<Patient> patients = patientPersistenceService.findByIdentificationNumberContaining(identificationNumber);
            // 2. Limitar a 15 resultados máximo
            List<Patient> limitedPatients = patients.stream()
                    .limit(15).toList();
            return ResponseEntity.ok(limitedPatients);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Error searching patients: " + e.getMessage() + "\"}");
        }
    }
}
