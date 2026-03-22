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

public class DoctorTest {

    /**
     * Tests that the constructor with specialties and schedule initializes correctly.
     */
    @Test
    void constructorWithSpecialtiesAndScheduleTest() {

        User user = new User();
        Schedule schedule = new Schedule();
        List<SpecialtyEnum> specialties = new ArrayList<>();
        specialties.add(SpecialtyEnum.FISIOTERAPIA);

        Doctor doctor = new Doctor(
                1,
                DocumentTypeEnum.CC,
                "123456789",
                "Nico",
                "Fernandez",
                new Date(),
                "3001234567",
                user,
                specialties,
                schedule,
                false

        );


        assertEquals(user, doctor.getUser());

        assertNotNull(doctor.getScheduledAppointments());
        assertEquals(0, doctor.getScheduledAppointments().size());

        assertNotNull(doctor.getAttendedAppointments());
        assertEquals(0, doctor.getAttendedAppointments().size());

        assertEquals(specialties, doctor.getSpecialties());

        //the scheduled never should be null
        assertEquals(schedule, doctor.getSchedule());
    }

    /**
     * Tests that the full constructor with all lists provided initializes correctly.
     */
    @Test
    void constructorWithAllParametersTest() {

        User user = new User();
        Schedule schedule;

        List<DayOfWeek> days = new ArrayList<DayOfWeek>();

        List<IntervalList> intervals = new ArrayList<IntervalList>();

        int year = 2026;
        int weeksRepeat = 5;
        days.add(DayOfWeek.THURSDAY);


        for(int i = 0; i < days.size(); i++){

            IntervalList interval = new IntervalList();

            interval.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
            interval.addInterval(new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0)));

            intervals.add(interval);

        }

        schedule = new Schedule(days,intervals,weeksRepeat,year);

        List<SpecialtyEnum> specialties = new ArrayList<>();
        specialties.add(SpecialtyEnum.QUIROPRAXIA);

        List<Appointment> scheduled = new ArrayList<>();
        List<Appointment> attended = new ArrayList<>();

        Doctor doctor = new Doctor(
                2,
                DocumentTypeEnum.CC,
                "123456",
                "Luisa",
                "Rocha",
                new Date(),
                "3119876543",
                user,
                specialties,
                scheduled,
                attended,
                schedule,
                false
        );


        assertEquals("Luisa", doctor.getFirstName());
        assertEquals("Rocha", doctor.getLastName());
        assertEquals(specialties, doctor.getSpecialties());
        assertEquals(scheduled, doctor.getScheduledAppointments());
        assertEquals(attended, doctor.getAttendedAppointments());
        assertEquals(schedule, doctor.getSchedule());
    }

        /**
         * Tests that addAppointmentToAttend correctly adds an appointment to the doctor's scheduled appointments.
         */
        @Test
        void addAppointmentToAttendTrueTest () throws Exception {

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
                    false
            );

            System.out.println(patient.getAppointmentCount());

            //here we create the appointment and add it to the patient and the doctor
            Appointment appointment = new Appointment(doctor,nextMonday1,interval1,patient);

            assertEquals(1, doctor.getScheduledAppointments().size());
            assertTrue(doctor.getScheduledAppointments().contains(appointment));
        }

        /**
         * Tests that cancelAppointment returns true when the appointment exists.
         */
        @Test
        void cancelAppointmentTrueTest () throws Exception{

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
                    false
            );

            //here we create the appointment and add it to the patient and the doctor
            Appointment appointment = new Appointment(doctor,nextMonday1,interval1,patient);

            assertEquals(1, doctor.getScheduledAppointments().size());

            // Cancel the appointment and verify it returns true
            assertTrue(doctor.cancelAppointment(appointment));

            assertEquals(0, doctor.getScheduledAppointments().size());
        }

        /**
         * Tests that cancelAppointment returns false when the appointment does not exist.
         */
        @Test
        void cancelAppointmentFalseTest () throws Exception{

            Patient patient = new Patient();
            LocalDate nextMonday1 = LocalDate.now().with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));
            Interval interval1 = new Interval(LocalTime.of(7, 0), LocalTime.of(8, 0));
            Interval interval2 = new Interval(LocalTime.of(9, 0), LocalTime.of(10, 0));
            User user1 = new User();
            List<SpecialtyEnum> specialties1 = new ArrayList<>();
            specialties1.add(SpecialtyEnum.FISIOTERAPIA);

            List<DayOfWeek> days = List.of(DayOfWeek.MONDAY);

            List<IntervalList> intervals = new ArrayList<>();
            IntervalList intervalList = new IntervalList();
            intervalList.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
            intervals.add(intervalList);
            Schedule schedule1 = new Schedule(days, intervals, 1, LocalDate.now().getYear());

            Doctor doctor1 = new Doctor(
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
                    false
            );

            Doctor doctor2 = new Doctor(
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
                    false
            );

            Appointment appointment1 = new Appointment(doctor1,nextMonday1,interval1,patient);
            Appointment appointment2 = new Appointment(doctor2,nextMonday1,interval2,patient);

            assertEquals(1, doctor1.getScheduledAppointments().size());
            assertFalse(doctor1.cancelAppointment(appointment2));
            assertEquals(1, doctor1.getScheduledAppointments().size());
        }

    }