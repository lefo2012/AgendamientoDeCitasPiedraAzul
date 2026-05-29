package co.edu.unicauca.BackendPiedraAzul.Users.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Authentication.keycloak.IKeycloakService;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.ConfDoctorDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.DoctorDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.PatientDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper.DoctorMapper;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper.PatientMapper;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IDoctorPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IPatientPersistenceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DoctorService implements IDoctorService {
    private final IKeycloakService keycloakService;

    private final IDoctorPersistenceService doctorService;

    private final DoctorMapper doctorMapper;

    public DoctorService(IKeycloakService keycloakService,
                         IDoctorPersistenceService doctorService, DoctorMapper doctorMapper) {
        this.keycloakService = keycloakService;
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public Doctor register(DoctorDTO doctorDto, String client_id) throws Exception {
        // 1️⃣ crear usuario en keycloak
        String keycloakId =
                keycloakService.createUserWithClientRoles(doctorDto.getUser(), doctorDto.getFirstName(), doctorDto.getLastName(), client_id);
        // 2️⃣ crear dominio
        Doctor doctor = doctorMapper.dtoToDomain(doctorDto);
        doctor.getUser().setKeycloakId(keycloakId);
        doctor.setActive(false);

        return doctorService.save(doctor);

    }

    @Override
    public List<Appointment> getAppointmentsByDoctorIDAndDate(Long doctorId, LocalDate date) throws Exception {

        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        Doctor doctor = doctorService.findById(doctorId);

        if (doctor == null) {
            throw new Exception("Doctor not found");
        }

        List<Appointment> sourceAppointments;

        sourceAppointments = doctor.getScheduledAppointments();
        sourceAppointments.addAll(doctor.getAttendedAppointments());

        return sourceAppointments.stream()
                .filter(app -> app.getAppointmentDate().isEqual(date))
                .toList();
    }

    @Override
    @Transactional
    public List<ConfDoctorDTO> getAllConfigDoctors() throws Exception {
        List<Doctor> doctors = doctorService.findAll();
        return doctors.stream()
                .map(doctorMapper::toConfDoctorDTO)
                .toList();
    }
}
