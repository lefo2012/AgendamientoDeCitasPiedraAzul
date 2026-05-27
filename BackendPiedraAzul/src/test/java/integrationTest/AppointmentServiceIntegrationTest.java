package integrationTest;

import co.edu.unicauca.BackendPiedraAzul.BackendPiedraAzulApplication;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ConfigureScheduleDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.IntervalDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.IntervalListDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ReserveAppointmentDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence.IAppointmentPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.IAppointmentService;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.IScheduleService;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.WhatsAppNotificationService;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.DoctorDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.PatientDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.UserDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IDoctorPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IPatientPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = BackendPiedraAzulApplication.class)
@ActiveProfiles("test")
@Transactional
class AppointmentServiceIntegrationTest {

    @Autowired
    private IAppointmentService appointmentService;

    @Autowired
    private IAppointmentPersistenceService appointmentPersistenceService;

    @Autowired
    private IDoctorPersistenceService doctorPersistenceService;

    @Autowired
    private IPatientPersistenceService patientPersistenceService;

    @Autowired
    private IScheduleService scheduleService;

    @MockitoBean
    private WhatsAppNotificationService whatsAppNotificationService;

    private Doctor doctor;
    private Patient patient;
    private LocalDate appointmentDate;

    @BeforeEach
    void setUp() throws Exception {
        appointmentDate = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        doctor = doctorPersistenceService.save(buildDoctorDto("1001"));
        patient = patientPersistenceService.save(buildPatientDto("2001"));

        scheduleService.configureSchedule(
                doctor.getId(),
                buildScheduleDto(appointmentDate.getDayOfWeek())
        );
    }

    @Test
    void shouldPersistAppointmentInPostgres() throws Exception {
        ReserveAppointmentDTO dto = new ReserveAppointmentDTO();
        dto.setIdDoctor(doctor.getId());
        dto.setIdPatient(patient.getId());
        dto.setAppointmentDate(appointmentDate);
        dto.setInterval(buildInterval("09:00", "09:30"));

        appointmentService.reserveAppointment(dto);

        List<Appointment> appointments = appointmentPersistenceService.findAll();
        boolean exists = appointments.stream().anyMatch(a ->
                a.getDoctor() != null && a.getPatient() != null &&
                        doctor.getId().equals(a.getDoctor().getId()) &&
                        patient.getId().equals(a.getPatient().getId()) &&
                        appointmentDate.equals(a.getAppointmentDate())
        );

        assertTrue(exists, "La cita debe persistirse en PostgreSQL");
    }

    private DoctorDTO buildDoctorDto(String suffix) {
        DoctorDTO dto = new DoctorDTO();
        dto.setDocumentType("CC");
        dto.setIdentificationNumber("DOC-" + suffix);
        dto.setFirstName("Ana");
        dto.setLastName("Medina");
        dto.setBirthDate("1990-01-01");
        dto.setPhone("3000000000");
        dto.setActive(true);
        dto.setGender("Femenino");
        dto.setCanSchedule(true);
        dto.setAppointmentInterval(buildInterval("08:00", "12:00"));
        dto.setUser(buildUserDto("doctor" + suffix + "@test.com", List.of("MEDICO")));
        return dto;
    }

    private PatientDTO buildPatientDto(String suffix) {
        PatientDTO dto = new PatientDTO();
        dto.setDocumentType("CC");
        dto.setIdentificationNumber("PAT-" + suffix);
        dto.setFirstName("Luis");
        dto.setLastName("Perez");
        dto.setBirthDate("1995-01-01");
        dto.setPhone("3100000000");
        dto.setActive(true);
        dto.setGender("Masculino");
        dto.setUser(buildUserDto("patient" + suffix + "@test.com", List.of("PACIENTE")));
        return dto;
    }

    private UserDTO buildUserDto(String email, List<String> roles) {
        UserDTO user = new UserDTO();
        user.setEmail(email);
        user.setPassword("test");
        user.setRoles(roles);
        return user;
    }

    private ConfigureScheduleDTO buildScheduleDto(DayOfWeek day) {
        ConfigureScheduleDTO dto = new ConfigureScheduleDTO();
        dto.setDays(List.of(day));
        dto.setSchedules(List.of(buildIntervalList("08:00", "12:00")));
        dto.setWeeksRepeat(1);
        dto.setYear(appointmentDate.getYear());
        return dto;
    }

    private IntervalListDTO buildIntervalList(String start, String end) {
        IntervalListDTO list = new IntervalListDTO();
        list.setIntervals(List.of(buildInterval(start, end)));
        return list;
    }

    private IntervalDTO buildInterval(String start, String end) {
        IntervalDTO interval = new IntervalDTO();
        interval.setStartTime(start);
        interval.setEndTime(end);
        return interval;
    }
}
