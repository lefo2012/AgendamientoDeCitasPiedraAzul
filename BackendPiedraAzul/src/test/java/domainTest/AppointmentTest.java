package domainTest;

import Appointments.domain.*;
import Appointments.persistence.entities.AppointmentEntity;
import Appointments.persistence.mapper.AppointmentMapper;
import Appointments.persistence.repository.AppointmentRepositoryJPA;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTest {


    private AppointmentRepositoryJPA appointmentRepository;

    private final AppointmentMapper appointmentMapper = AppointmentMapper.INSTANCE;

    @Test
    void saveAppointmentTest() throws Exception {
        // --- Preparar doctor ---
        User user = new User();
        List<SpecialtyEnum> specialties = new ArrayList<>();
        List<DayOfWeek> days = List.of(DayOfWeek.MONDAY);
        List<IntervalList> intervals = new ArrayList<>();
        IntervalList intervalList = new IntervalList();
        intervalList.addInterval(new Interval(LocalTime.of(7,0), LocalTime.of(13,0)));
        intervals.add(intervalList);
        Schedule schedule = new Schedule(days, intervals, 1, LocalDate.now().getYear());

        Doctor doctor = new Doctor(
                1L,
                DocumentTypeEnum.CC,
                "123456789",
                "Angela",
                "Mia",
                new java.util.Date(),
                "3001112222",
                user,
                specialties,
                schedule
        );

        // --- Preparar paciente ---
        Patient patient = new Patient();
        patient.setPendingAppointments(new ArrayList<>());

        // --- Crear cita ---
        LocalDate nextMonday = LocalDate.now().with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));
        Interval interval = new Interval(LocalTime.of(7,0), LocalTime.of(8,0));
        Appointment appointment = new Appointment().scheduleAppointment(doctor, nextMonday, interval, patient);

        // --- Mappear a entidad JPA (suponiendo que tienes AppointmentMapper) ---
        AppointmentEntity entity = appointmentMapper.toEntity(appointment);

        // --- Guardar en base de datos ---
        AppointmentEntity saved = appointmentRepository.save(entity);

        // --- Recuperar de base de datos ---
        AppointmentEntity found = appointmentRepository.findById(saved.getId()).orElse(null);

        // --- Validaciones ---
        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getAppointmentDate(), found.getAppointmentDate());
        assertEquals(saved.getInterval().getStart(), found.getInterval().getStart());
        assertEquals(saved.getInterval().getEnd(), found.getInterval().getEnd());
        assertEquals(saved.getAppointmentStatus(), found.getAppointmentStatus());
    }

    /**
     * Test que verifica que scheduleAppointment asigna correctamente doctor, paciente y fecha
     */
//    @Test
//    void scheduleAppointmentTest() throws Exception {
//        // Preparar doctor
//        User user = new User();
//        List<SpecialtyEnum> specialties = new ArrayList<>();
//        List<DayOfWeek> days = List.of(DayOfWeek.MONDAY);
//        List<IntervalList> intervals = new ArrayList<>();
//        IntervalList intervalList = new IntervalList();
//        intervalList.addInterval(new Interval(LocalTime.of(7,0), LocalTime.of(13,0)));
//        intervals.add(intervalList);
//        Schedule schedule = new Schedule(days, intervals, 1, LocalDate.now().getYear());
//
//        Doctor doctor = new Doctor(
//                1,
//                DocumentTypeEnum.CC,
//                "123456789",
//                "Angela",
//                "Mia",
//                new java.util.Date(),
//                "3001112222",
//                user,
//                specialties,
//                schedule
//        );
//
//        // Preparar paciente
//        Patient patient = new Patient();
//        patient.setPendingAppointments(new ArrayList<>());
//
//        // Fecha válida para schedule
//        LocalDate nextMonday = LocalDate.now().with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));
//        Interval interval = new Interval(LocalTime.of(7,0), LocalTime.of(8,0));
//
//        // Agendar cita
//        Appointment appointment = new Appointment().scheduleAppointment(doctor, nextMonday, interval, patient);
//
//        // Verificar que la cita se asignó correctamente
//        assertEquals(doctor, appointment.getDoctor());
//        assertEquals(patient, appointment.getPatient());
//        assertEquals(nextMonday, appointment.getAppointmentDate());
//        assertEquals(interval, appointment.getInterval());
//        assertEquals(AppointmentStatusEnum.AGENDADA, appointment.getAppointmentStatus());
//
//        // Verificar que el doctor contiene la cita
//        assertTrue(doctor.getScheduledAppointments().contains(appointment));
//
//        // Verificar que el paciente contiene la cita pendiente
//        assertTrue(patient.getPendingAppointments().contains(appointment));
//    }

    /**
     * Test de getters y setters
     */
//    @Test
//    void gettersAndSettersTest() {
//        Appointment appointment = new Appointment();
//        Doctor doctor = new Doctor();
//        Patient patient = new Patient();
//        LocalDate date = LocalDate.of(2026, 3, 16);
//        Interval interval = new Interval(LocalTime.of(9,0), LocalTime.of(10,0));
//
//        appointment.setDoctor(doctor);
//        appointment.setPatient(patient);
//        appointment.setAppointmentDate(date);
//        appointment.setInterval(interval);
//        appointment.setAppointmentStatus(AppointmentStatusEnum.AGENDADA);
//        appointment.setId(100L);
//
//        assertEquals(doctor, appointment.getDoctor());
//        assertEquals(patient, appointment.getPatient());
//        assertEquals(date, appointment.getAppointmentDate());
//        assertEquals(interval, appointment.getInterval());
//        assertEquals(AppointmentStatusEnum.AGENDADA, appointment.getAppointmentStatus());
//        assertEquals(100L, appointment.getId());
//    }
}
