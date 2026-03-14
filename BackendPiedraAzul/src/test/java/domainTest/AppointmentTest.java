package domainTest;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.*;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.AppointmentEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.AppointmentMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.repository.AppointmentRepositoryJPA;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AppointmentTest {


    /**
     * Test que verifica que scheduleAppointment asigna correctamente doctor, paciente y fecha
     */
    @Test
    void scheduleAppointmentTest() throws Exception {
        // Preparar doctor
        User user = new User();
        List<SpecialtyEnum> specialties = new ArrayList<>();
        List<DayOfWeek> days = List.of(DayOfWeek.MONDAY);
        List<IntervalList> intervals = new ArrayList<>();
        IntervalList intervalList = new IntervalList();
        intervalList.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
        intervals.add(intervalList);
        Schedule schedule = new Schedule(days, intervals, 1, LocalDate.now().getYear());

        Doctor doctor = new Doctor(
                1,
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

        // Preparar paciente
        Patient patient = new Patient();
        patient.setPendingAppointments(new ArrayList<>());

        // Fecha válida para schedule
        LocalDate nextMonday = LocalDate.now().with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));
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
}
