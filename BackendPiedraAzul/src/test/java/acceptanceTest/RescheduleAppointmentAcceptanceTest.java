package acceptanceTest;

import co.edu.unicauca.BackendPiedraAzul.BackendPiedraAzulApplication;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.AppointmentStatusEnum;
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
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = BackendPiedraAzulApplication.class)
@ActiveProfiles("test")
@Transactional
class RescheduleAppointmentAcceptanceTest {

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

    private Doctor originalDoctor;
    private Doctor newDoctor;
    private Patient patient;
    private LocalDate appointmentDate;

    @BeforeEach
    void setUp() throws Exception {
        appointmentDate = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        originalDoctor = doctorPersistenceService.save(buildDoctorDto("5001", "Laura", "Rojas"));
        newDoctor = doctorPersistenceService.save(buildDoctorDto("5002", "Mario", "Suarez"));
        patient = patientPersistenceService.save(buildPatientDto("6001"));

        ConfigureScheduleDTO schedule = buildScheduleDto(appointmentDate.getDayOfWeek(), appointmentDate.getYear());
        scheduleService.configureSchedule(originalDoctor.getId(), schedule);
        scheduleService.configureSchedule(newDoctor.getId(), schedule);
    }

    @Test
    void DADO_una_cita_agendada_CUANDO_reagendo_ENTONCES_la_original_queda_ATENDIDA_y_la_nueva_AGENDADA() throws Exception {
        // DADO una cita agendada
        ReserveAppointmentDTO initial = new ReserveAppointmentDTO();
        initial.setIdDoctor(originalDoctor.getId());
        initial.setIdPatient(patient.getId());
        initial.setAppointmentDate(appointmentDate);
        initial.setInterval(buildInterval("09:00", "09:30"));
        appointmentService.reserveAppointment(initial);

        Appointment originalAppointment = appointmentPersistenceService.findAll().stream()
                .filter(a -> a.getDoctor() != null && originalDoctor.getId().equals(a.getDoctor().getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(originalAppointment, "Debe existir la cita original");

        // CUANDO reagendo la cita
        ReserveAppointmentDTO reschedule = new ReserveAppointmentDTO();
        reschedule.setId(originalAppointment.getId());
        reschedule.setIdDoctor(newDoctor.getId());
        reschedule.setIdPatient(patient.getId());
        reschedule.setAppointmentDate(appointmentDate);
        reschedule.setInterval(buildInterval("10:00", "10:30"));
        appointmentService.reScheduleDoctor(reschedule);

        // ENTONCES la cita original queda ATENDIDA y se crea una nueva AGENDADA
        Appointment updatedOriginal = appointmentPersistenceService.findById(originalAppointment.getId());
        assertEquals(AppointmentStatusEnum.ATENDIDA, updatedOriginal.getAppointmentStatus());

        Appointment newAppointment = appointmentPersistenceService.findAll().stream()
                .filter(a -> a.getDoctor() != null && newDoctor.getId().equals(a.getDoctor().getId()))
                .filter(a -> appointmentDate.equals(a.getAppointmentDate()))
                .findFirst()
                .orElse(null);

        assertNotNull(newAppointment, "Debe existir la nueva cita agendada");
        assertEquals(AppointmentStatusEnum.AGENDADA, newAppointment.getAppointmentStatus());
    }

    private DoctorDTO buildDoctorDto(String suffix, String firstName, String lastName) {
        DoctorDTO dto = new DoctorDTO();
        dto.setDocumentType("CC");
        dto.setIdentificationNumber("DOC-" + suffix);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setBirthDate("1985-02-02");
        dto.setPhone("3000000002");
        dto.setActive(true);
        dto.setGender("Masculino");
        dto.setCanSchedule(true);
        dto.setAppointmentInterval(buildInterval("08:00", "12:00"));
        dto.setUser(buildUserDto("doctor" + suffix + "@test.com", List.of("MEDICO")));
        return dto;
    }

    private PatientDTO buildPatientDto(String suffix) {
        PatientDTO dto = new PatientDTO();
        dto.setDocumentType("CC");
        dto.setIdentificationNumber("PAT-" + suffix);
        dto.setFirstName("Sofia");
        dto.setLastName("Martinez");
        dto.setBirthDate("1992-03-03");
        dto.setPhone("3100000002");
        dto.setActive(true);
        dto.setGender("Femenino");
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

    private ConfigureScheduleDTO buildScheduleDto(DayOfWeek day, int year) {
        ConfigureScheduleDTO dto = new ConfigureScheduleDTO();
        dto.setDays(List.of(day));
        dto.setSchedules(List.of(buildIntervalList("08:00", "12:00")));
        dto.setWeeksRepeat(1);
        dto.setYear(year);
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
