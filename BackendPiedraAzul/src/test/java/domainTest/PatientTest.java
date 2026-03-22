package domainTest;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.DocumentTypeEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.*;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PatientTest {

    /**
     * Test constructor with all parameters
     */
    @Test
    void constructorWithAllParametersTest() throws Exception {
        User user = new User();
        List<Appointment> pending = new ArrayList<>();
        List<Appointment> past = new ArrayList<>();
        MedicalHistory history = new MedicalHistory();

        Patient patient = new Patient(
                1L,
                DocumentTypeEnum.CC,
                "987654321",
                "Lu",
                "Fer",
                new Date(),
                "3105551234",
                true,
                user,
                0,
                history
        );

        assertEquals(user, patient.getUser());
        assertEquals(pending, patient.getPendingAppointments());
        assertEquals(past, patient.getPastAppointments());
        assertEquals(history, patient.getMedicalHistory());
        assertEquals(0, patient.getAppointmentCount());
    }

    /**
     * Test addPendingAppointment add appointments correctly and updates appointment count
     */
    @Test
    void addPendingAppointmentTest() throws Exception {
        Patient patient = new Patient();
        LocalDate nextMonday1 = LocalDate.now().with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));
        Interval interval1 = new Interval(LocalTime.of(7, 0), LocalTime.of(8, 0));

        User user1 = new User();
        List<SpecialtyEnum> specialties1 = new ArrayList<>();
        specialties1.add(SpecialtyEnum.FISIOTERAPIA);

        List<DayOfWeek> days = List.of(DayOfWeek.MONDAY);

        List<IntervalList> intervals = new ArrayList<>();
        IntervalList intervalList = new IntervalList();
        intervalList.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
        intervals.add(intervalList);
        Schedule schedule1 = new Schedule(days, intervals, 1, LocalDate.now().getYear());
        Doctor doctor = new Doctor(
                1,
                DocumentTypeEnum.CC,
                "123456789",
                "Angela",
                "Mia",
                new java.util.Date(),
                "3001112222",
                user1,
                specialties1,
                schedule1,
                false,
                new Interval( LocalTime.of(7, 0), LocalTime.of(13, 0))
        );

        System.out.println(patient.getAppointmentCount());
        //here we create the appointment and add it to the patient
        Appointment appointment1 = new Appointment(doctor,nextMonday1,interval1,patient);

        System.out.println(patient.getAppointmentCount());

        assertNotNull(patient.getPendingAppointments());
        assertEquals(1, patient.getPendingAppointments().size());
        assertTrue(patient.getPendingAppointments().contains(appointment1));
        assertEquals(1, patient.getAppointmentCount());

        // Add another appointment
        LocalDate nextMonday2 = LocalDate.now().with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));
        Interval interval2 = new Interval(LocalTime.of(9, 0), LocalTime.of(10, 0));

        Appointment appointment2 = new Appointment(doctor,nextMonday2,interval2,patient);

        assertEquals(2, patient.getPendingAppointments().size());
        assertEquals(2, patient.getAppointmentCount());
        assertTrue(patient.getPendingAppointments().contains(appointment2));
    }

}