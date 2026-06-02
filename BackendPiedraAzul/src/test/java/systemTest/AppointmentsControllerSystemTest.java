package systemTest;

import co.edu.unicauca.BackendPiedraAzul.BackendPiedraAzulApplication;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ConfigureScheduleDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.IntervalDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.IntervalListDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ReserveAppointmentDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence.IAppointmentPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.IScheduleService;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases.WhatsAppNotificationService;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.DoctorDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.PatientDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.UserDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IDoctorPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IPatientPersistenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureDataSourceInitialization;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BackendPiedraAzulApplication.class)
@ActiveProfiles("test")
@AutoConfigureDataSourceInitialization
@Transactional
class AppointmentsControllerSystemTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    @Autowired
    private IDoctorPersistenceService doctorPersistenceService;

    @Autowired
    private IPatientPersistenceService patientPersistenceService;

    @Autowired
    private IScheduleService scheduleService;

    @Autowired
    private IAppointmentPersistenceService appointmentPersistenceService;

    @MockitoBean
    private WhatsAppNotificationService whatsAppNotificationService;

    private Doctor doctor;
    private Patient patient;
    private LocalDate appointmentDate;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        appointmentDate = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.TUESDAY));

        doctor = doctorPersistenceService.save(buildDoctorDto("3001"));
        patient = patientPersistenceService.save(buildPatientDto("4001"));

        scheduleService.configureSchedule(
                doctor.getId(),
                buildScheduleDto(appointmentDate.getDayOfWeek(), appointmentDate.getYear())
        );
    }

    @Test
    @WithMockUser(roles = "MEDICO")
    void shouldReserveCancelAndListAppointments() throws Exception {
        ReserveAppointmentDTO reserve = new ReserveAppointmentDTO();
        reserve.setIdDoctor(doctor.getId());
        reserve.setIdPatient(patient.getId());
        reserve.setAppointmentDate(appointmentDate);
        reserve.setInterval(buildInterval("09:00", "09:30"));

        mockMvc.perform(post("/api/appointments/reserve")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reserve)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Reserva realizada con éxito")));

        List<Appointment> appointments = appointmentPersistenceService.findAll();
        assertTrue(!appointments.isEmpty(), "Debe existir al menos una cita registrada");

        Long appointmentId = appointments.getFirst().getId();

        mockMvc.perform(put("/api/appointments/cancel/{id}", appointmentId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/appointments/getAppointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }

    private DoctorDTO buildDoctorDto(String suffix) {
        DoctorDTO dto = new DoctorDTO();
        dto.setDocumentType("CC");
        dto.setIdentificationNumber("DOC-" + suffix);
        dto.setFirstName("Marta");
        dto.setLastName("Lopez");
        dto.setBirthDate("1989-05-05");
        dto.setPhone("3000000001");
        dto.setActive(true);
        dto.setGender("Femenino");
        dto.setCanSchedule(true);
        dto.setAppointmentInterval(buildInterval("00:00", "00:30"));
        dto.setUser(buildUserDto("doctor" + suffix + "@test.com", List.of("MEDICO")));
        return dto;
    }

    private PatientDTO buildPatientDto(String suffix) {
        PatientDTO dto = new PatientDTO();
        dto.setDocumentType("CC");
        dto.setIdentificationNumber("PAT-" + suffix);
        dto.setFirstName("Carlos");
        dto.setLastName("Diaz");
        dto.setBirthDate("1994-08-08");
        dto.setPhone("3100000001");
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
