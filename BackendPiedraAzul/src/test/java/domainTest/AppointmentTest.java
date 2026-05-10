package domainTest;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.DocumentTypeEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.GenderEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.*;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AppointmentTest {

    private Doctor buildDoctorWithMondayAvailability() {
        User user = new User();
        List<SpecialtyEnum> specialties = new ArrayList<>();
        List<DayOfWeek> days = List.of(DayOfWeek.MONDAY);
        List<IntervalList> intervals = new ArrayList<>();

        IntervalList intervalList = new IntervalList();
        intervalList.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
        intervals.add(intervalList);

        Schedule schedule = new Schedule(days, intervals, 20, LocalDate.now().getYear());

        return new Doctor(
                1,
                DocumentTypeEnum.CC,
                "123456789",
                "Angela",
                "Mia",
                new java.util.Date(),
                "3001112222",
                user,
                GenderEnum.Femenino,
                specialties,
                schedule,
                false,
                new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0))
        );
    }

    private LocalDate nextWorkingMonday(Schedule schedule) {
        LocalDate monday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        while (schedule.getHolidays().contains(monday)) {
            monday = monday.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }
        return monday;
    }


    /**
     * Test que verifica que scheduleAppointment asigna correctamente doctor, paciente y fecha
     */
    @Test
    void scheduleAppointmentTest() throws Exception {
        Doctor doctor = buildDoctorWithMondayAvailability();

        // Preparar paciente
        Patient patient = new Patient();
        patient.setPendingAppointments(new ArrayList<>());

        // Fecha válida para schedule
        LocalDate nextMonday = nextWorkingMonday(doctor.getSchedule());
        Interval interval = new Interval(LocalTime.of(7, 0), LocalTime.of(8, 0));

        // Agendar cita
        Appointment appointment = new Appointment(doctor, nextMonday, interval, patient);

        // Verificar que la cita se asignó correctamente
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(patient, appointment.getPatient());
        assertEquals(nextMonday, appointment.getAppointmentDate());
        assertEquals(interval, appointment.getInterval());
        assertEquals(AppointmentStatusEnum.AGENDADA, appointment.getAppointmentStatus());

        // Verificar que el doctor contiene la cita
        assertTrue(doctor.getScheduledAppointments().contains(appointment));

        // Verificar que el paciente contiene la cita pendiente
        assertTrue(patient.getPendingAppointments().contains(appointment));
    }

    @Test
    void scheduleTwoAppointmentsDifferentIntervalsSameDayTest() throws Exception {
        Doctor doctor = buildDoctorWithMondayAvailability();
        Patient patient = new Patient();
        LocalDate nextMonday = nextWorkingMonday(doctor.getSchedule());

        Appointment first = new Appointment(doctor, nextMonday, new Interval(LocalTime.of(7, 0), LocalTime.of(8, 0)), patient);
        Appointment second = new Appointment(doctor, nextMonday, new Interval(LocalTime.of(8, 0), LocalTime.of(9, 0)), patient);

        assertEquals(2, doctor.getScheduledAppointments().size());
        assertTrue(doctor.getScheduledAppointments().contains(first));
        assertTrue(doctor.getScheduledAppointments().contains(second));
        assertEquals(2, patient.getPendingAppointments().size());
        assertEquals(2, patient.getAppointmentCount());
    }

    @Test
    void scheduleAppointmentWithIntervalOutsideAvailabilityThrowsTest() {
        Doctor doctor = buildDoctorWithMondayAvailability();
        Patient patient = new Patient();
        LocalDate nextMonday = nextWorkingMonday(doctor.getSchedule());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Appointment(doctor, nextMonday, new Interval(LocalTime.of(18, 0), LocalTime.of(19, 0)), patient)
        );

        assertTrue(exception.getMessage().contains("INTERVAL NOT AVAILABLE"));
        assertEquals(0, doctor.getScheduledAppointments().size());
        assertEquals(0, patient.getPendingAppointments().size());
    }

    @Test
    void scheduleOverlappingAppointmentThrowsTest() throws Exception {
        Doctor doctor = buildDoctorWithMondayAvailability();
        Patient patient = new Patient();
        LocalDate nextMonday = nextWorkingMonday(doctor.getSchedule());

        new Appointment(doctor, nextMonday, new Interval(LocalTime.of(9, 0), LocalTime.of(10, 0)), patient);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Appointment(doctor, nextMonday, new Interval(LocalTime.of(9, 30), LocalTime.of(10, 30)), patient)
        );

        assertTrue(exception.getMessage().contains("INTERVAL NOT AVAILABLE"));
        assertEquals(1, doctor.getScheduledAppointments().size());
        assertEquals(1, patient.getPendingAppointments().size());
    }

    @Test
    void scheduleAppointmentOnNonAvailableDayThrowsTest() {
        Doctor doctor = buildDoctorWithMondayAvailability();
        Patient patient = new Patient();
        LocalDate nextTuesday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.TUESDAY));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Appointment(doctor, nextTuesday, new Interval(LocalTime.of(9, 0), LocalTime.of(10, 0)), patient)
        );

        assertTrue(exception.getMessage().contains("INTERVAL NOT AVAILABLE"));
        assertEquals(0, doctor.getScheduledAppointments().size());
        assertEquals(0, patient.getPendingAppointments().size());
    }

    @Test
    void scheduleAppointmentOnHolidayThrowsTest() {
        Doctor doctor = buildDoctorWithMondayAvailability();
        Patient patient = new Patient();
        // Asumir que hay un festivo, e.g., 1 de enero
        LocalDate holiday = LocalDate.of(LocalDate.now().getYear(), 1, 1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Appointment(doctor, holiday, new Interval(LocalTime.of(9, 0), LocalTime.of(10, 0)), patient)
        );

        assertTrue(exception.getMessage().contains("INTERVAL NOT AVAILABLE"));
        assertEquals(0, doctor.getScheduledAppointments().size());
        assertEquals(0, patient.getPendingAppointments().size());
    }

    @Test
    void scheduleAppointmentWithInvalidIntervalThrowsTest() {
        Doctor doctor = buildDoctorWithMondayAvailability();
        Patient patient = new Patient();
        LocalDate nextMonday = nextWorkingMonday(doctor.getSchedule());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Appointment(doctor, nextMonday, new Interval(LocalTime.of(10, 0), LocalTime.of(9, 0)), patient)
        );

        assertTrue(exception.getMessage().contains("End must be after start"));
        assertEquals(0, doctor.getScheduledAppointments().size());
        assertEquals(0, patient.getPendingAppointments().size());
    }

    @Test
    void constructorWithAllParametersTest() {
        Doctor doctor = buildDoctorWithMondayAvailability();
        Patient patient = new Patient();
        LocalDate date = LocalDate.now();
        Interval interval = new Interval(LocalTime.of(8, 0), LocalTime.of(9, 0));

        Appointment appointment = new Appointment(1L, doctor, patient, date, AppointmentStatusEnum.AGENDADA, interval);

        assertEquals(1L, appointment.getId());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(patient, appointment.getPatient());
        assertEquals(date, appointment.getAppointmentDate());
        assertEquals(AppointmentStatusEnum.AGENDADA, appointment.getAppointmentStatus());
        assertEquals(interval, appointment.getInterval());
    }

    @Test
    void defaultConstructorTest() {
        Appointment appointment = new Appointment();

        assertNull(appointment.getId());
        assertNull(appointment.getDoctor());
        assertNull(appointment.getPatient());
        assertNull(appointment.getAppointmentDate());
        assertNull(appointment.getAppointmentStatus());
        assertNull(appointment.getInterval());
    }

    @Test
    void settersAndGettersTest() {
        Appointment appointment = new Appointment();
        Doctor doctor = buildDoctorWithMondayAvailability();
        Patient patient = new Patient();
        LocalDate date = LocalDate.now();
        Interval interval = new Interval(LocalTime.of(8, 0), LocalTime.of(9, 0));

        appointment.setId(2L);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(date);
        appointment.setAppointmentStatus(AppointmentStatusEnum.ATENDIDA);
        appointment.setInterval(interval);

        assertEquals(2L, appointment.getId());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(patient, appointment.getPatient());
        assertEquals(date, appointment.getAppointmentDate());
        assertEquals(AppointmentStatusEnum.ATENDIDA, appointment.getAppointmentStatus());
        assertEquals(interval, appointment.getInterval());
    }

    @Test
    void multiplePatientsSameDoctorTest() throws Exception {
        Doctor doctor = buildDoctorWithMondayAvailability();
        Patient patient1 = new Patient();
        Patient patient2 = new Patient();
        LocalDate nextMonday = nextWorkingMonday(doctor.getSchedule());

        Appointment app1 = new Appointment(doctor, nextMonday, new Interval(LocalTime.of(7, 0), LocalTime.of(8, 0)), patient1);
        Appointment app2 = new Appointment(doctor, nextMonday, new Interval(LocalTime.of(8, 0), LocalTime.of(9, 0)), patient2);

        assertEquals(2, doctor.getScheduledAppointments().size());
        assertTrue(doctor.getScheduledAppointments().contains(app1));
        assertTrue(doctor.getScheduledAppointments().contains(app2));
        assertEquals(1, patient1.getPendingAppointments().size());
        assertEquals(1, patient2.getPendingAppointments().size());
        assertEquals(1, patient1.getAppointmentCount());
        assertEquals(1, patient2.getAppointmentCount());
    }

    @Test
    void cancelAppointmentAndRescheduleTest() throws Exception {
        Doctor doctor = buildDoctorWithMondayAvailability();
        Patient patient = new Patient();
        LocalDate nextMonday = nextWorkingMonday(doctor.getSchedule());
        Interval interval = new Interval(LocalTime.of(7, 0), LocalTime.of(8, 0));

        Appointment appointment = new Appointment(doctor, nextMonday, interval, patient);
        assertEquals(1, doctor.getScheduledAppointments().size());

        doctor.cancelAppointment(appointment);
        assertEquals(0, doctor.getScheduledAppointments().size());
        assertEquals(1, patient.getPendingAppointments().size()); // Aún pendiente en paciente

        // Ahora reagendar
        Appointment newAppointment = new Appointment(doctor, nextMonday, interval, patient);
        assertEquals(1, doctor.getScheduledAppointments().size());
        assertTrue(doctor.getScheduledAppointments().contains(newAppointment));
        assertEquals(2, patient.getPendingAppointments().size()); // Nueva cita agregada
    }

    @Test
    void appointmentStatusChangeTest() throws Exception {
        Doctor doctor = buildDoctorWithMondayAvailability();
        Patient patient = new Patient();
        LocalDate nextMonday = nextWorkingMonday(doctor.getSchedule());
        Interval interval = new Interval(LocalTime.of(7, 0), LocalTime.of(8, 0));

        Appointment appointment = new Appointment(doctor, nextMonday, interval, patient);
        assertEquals(AppointmentStatusEnum.AGENDADA, appointment.getAppointmentStatus());

        appointment.setAppointmentStatus(AppointmentStatusEnum.ATENDIDA);
        assertEquals(AppointmentStatusEnum.ATENDIDA, appointment.getAppointmentStatus());

        appointment.setAppointmentStatus(AppointmentStatusEnum.CANCELADA);
        assertEquals(AppointmentStatusEnum.CANCELADA, appointment.getAppointmentStatus());
    }

    @Test
    void patientAppointmentCountTest() throws Exception {
        Doctor doctor = buildDoctorWithMondayAvailability();
        Patient patient = new Patient();
        LocalDate nextMonday = nextWorkingMonday(doctor.getSchedule());

        assertEquals(0, patient.getAppointmentCount());

        new Appointment(doctor, nextMonday, new Interval(LocalTime.of(7, 0), LocalTime.of(8, 0)), patient);
        assertEquals(1, patient.getAppointmentCount());

        new Appointment(doctor, nextMonday, new Interval(LocalTime.of(8, 0), LocalTime.of(9, 0)), patient);
        assertEquals(2, patient.getAppointmentCount());
    }

    @Test
    void doctorWithoutScheduleTest() {
        Doctor doctor = new Doctor(); // Sin schedule
        Patient patient = new Patient();
        LocalDate date = LocalDate.now();
        Interval interval = new Interval(LocalTime.of(9, 0), LocalTime.of(10, 0));

        Exception exception = assertThrows(
                Exception.class,
                () -> new Appointment(doctor, date, interval, patient)
        );

        // Debería fallar porque schedule es null
        assertNotNull(exception);
    }

    @Test
    void intervalOverlapsTest() {
        Interval interval1 = new Interval(LocalTime.of(9, 0), LocalTime.of(10, 0));
        Interval interval2 = new Interval(LocalTime.of(9, 30), LocalTime.of(10, 30));
        Interval interval3 = new Interval(LocalTime.of(10, 0), LocalTime.of(11, 0));

        assertTrue(interval1.overlaps(interval2));
        assertFalse(interval1.overlaps(interval3));
    }

    @Test
    void intervalIsWithinTest() {
        Interval container = new Interval(LocalTime.of(8, 0), LocalTime.of(12, 0));
        Interval within = new Interval(LocalTime.of(9, 0), LocalTime.of(10, 0));
        Interval outside = new Interval(LocalTime.of(7, 0), LocalTime.of(9, 0));

        assertTrue(within.isWithin(container));
        assertFalse(outside.isWithin(container));
    }
}
