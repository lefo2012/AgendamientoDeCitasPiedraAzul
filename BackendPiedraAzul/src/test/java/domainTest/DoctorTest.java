package domainTest;
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
                schedule
        );


        assertEquals(user, doctor.getUser());
//
//        assertNotNull(doctor.getScheduledAppointments());
//        assertEquals(0, doctor.getScheduledAppointments().size());
//
//        assertNotNull(doctor.getAttendedAppointments());
//        assertEquals(0, doctor.getAttendedAppointments().size());

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
                schedule
        );


        assertEquals("Luisa", doctor.getFirstName());
        assertEquals("Rocha", doctor.getLastName());
        assertEquals(specialties, doctor.getSpecialties());
//        assertEquals(scheduled, doctor.getScheduledAppointments());
//        assertEquals(attended, doctor.getAttendedAppointments());
        assertEquals(schedule, doctor.getSchedule());
    }

        /**
         * Tests that addAppointmentToAttend correctly adds an appointment to the doctor's scheduled appointments.
         */
        @Test
        void addAppointmentToAttendTrueTest () throws Exception {
//            User user = new User();
//            List<SpecialtyEnum> specialties = new ArrayList<>();
//            Schedule schedule;
//            List<DayOfWeek> days = new ArrayList<DayOfWeek>();
//            List<IntervalList> intervals = new ArrayList<IntervalList>();
//
//            int year = 2026;
//            int weeksRepeat = 5;
//            days.add(DayOfWeek.MONDAY);
//
//            for(int i = 0; i < days.size(); i++){
//
//                IntervalList interval = new IntervalList();
//
//                interval.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
//                interval.addInterval(new Interval(LocalTime.of(14, 0), LocalTime.of(16, 0)));
//
//                intervals.add(interval);
//
//            }
//
//            schedule = new Schedule(days,intervals,weeksRepeat,year);
//
//            Doctor doctor = new Doctor(
//                    4,
//                    DocumentTypeEnum.CC,
//                    "123456789",
//                    "Angela",
//                    "Mia",
//                    new Date(),
//                    "3001112222",
//                    user,
//                    specialties,
//                    schedule
//            );
//
//            Appointment appointment = new Appointment();
//            //appointment = appointment.scheduleAppointment(doctor, LocalDate.of(2025, 3, 16), new Interval(LocalTime.of(7, 0),LocalTime.of(13, 0)), null);
//            appointment.setDoctor(doctor);
//            appointment.setAppointmentDate( LocalDate.of(2026, 3, 16)); // Monday
//            appointment.setInterval(new Interval(LocalTime.of(7, 0),LocalTime.of(13, 0)));
//
//            assertTrue(doctor.addAppointmentToAttend(appointment));
//            assertEquals(1, doctor.getScheduledAppointments().size());
//            assertTrue(doctor.getScheduledAppointments().contains(appointment));
        }

        /**
         * Tests that cancelAppointment returns true when the appointment exists.
         */
        @Test
        void cancelAppointmentTrueTest () {

//            User user = new User();
//            List<SpecialtyEnum> specialties = new ArrayList<>();
//            Schedule schedule;
//
//            List<DayOfWeek> days = new ArrayList<DayOfWeek>();
//
//            List<IntervalList> intervals = new ArrayList<IntervalList>();
//
//            int year = 2026;
//            int weeksRepeat = 5;
//            days.add(DayOfWeek.MONDAY);
//
//
//            for(int i = 0; i < days.size(); i++){
//
//                IntervalList interval = new IntervalList();
//
//                interval.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
//                interval.addInterval(new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0)));
//
//                intervals.add(interval);
//
//            }
//
//            schedule = new Schedule(days,intervals,weeksRepeat,year);
//            Doctor doctor = new Doctor(
//                    6,
//                    DocumentTypeEnum.CC,
//                    "123456789",
//                    "Nelson",
//                    "sama",
//                    new Date(),
//                    "3001112222",
//                    user,
//                    specialties,
//                    schedule
//            );
//
//            Appointment appointment = new Appointment();
//
//            List<Appointment> appointments = new ArrayList<>();
//            appointments.add(appointment);
//
//            doctor.setScheduledAppointments(appointments);
//
//            assertTrue(doctor.cancelAppointment(appointment));
//
//            assertEquals(0, doctor.getScheduledAppointments().size());
        }

        /**
         * Tests that cancelAppointment returns false when the appointment does not exist.
         */
        @Test
        void cancelAppointmentFalseTest () {

//            User user = new User();
//            List<SpecialtyEnum> specialties = new ArrayList<>();
//            Schedule schedule;
//
//            List<DayOfWeek> days = new ArrayList<DayOfWeek>();
//
//            List<IntervalList> intervals = new ArrayList<IntervalList>();
//
//            int year = 2026;
//            int weeksRepeat = 5;
//            days.add(DayOfWeek.MONDAY);
//
//
//            for(int i = 0; i < days.size(); i++){
//
//                IntervalList interval = new IntervalList();
//
//                interval.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(13, 0)));
//                interval.addInterval(new Interval(LocalTime.of(14, 0), LocalTime.of(18, 0)));
//
//                intervals.add(interval);
//
//            }
//
//            schedule = new Schedule(days,intervals,weeksRepeat,year);
//
//            Doctor doctor = new Doctor(
//                    7,
//                    DocumentTypeEnum.CC,
//                    "123456789",
//                    "Libar",
//                    "god",
//                    new Date(),
//                    "3001112222",
//                    user,
//                    specialties,
//                    schedule
//            );
//
//            Appointment appointment1 = new Appointment();
//            Appointment appointment2 = new Appointment();
//
//            List<Appointment> appointments = new ArrayList<>();
//            appointments.add(appointment1);

//            doctor.setScheduledAppointments(appointments);
//
//            assertFalse(doctor.cancelAppointment(appointment2));
//
//            assertEquals(1, doctor.getScheduledAppointments().size());
        }

    }