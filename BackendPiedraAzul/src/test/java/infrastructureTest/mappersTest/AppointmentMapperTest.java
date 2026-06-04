package infrastructureTest.mappersTest;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.DocumentTypeEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.GenderEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.*;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.AppointmentEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.AppointmentMapper;
import co.edu.unicauca.BackendPiedraAzul.BackendPiedraAzulApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = BackendPiedraAzulApplication.class)
public class AppointmentMapperTest {

    @Autowired
    AppointmentMapper appointmentMapper;

    @Test
    public void AppointmentMapperTest() throws Exception {

        User user = new User();
        List<SpecialtyEnum> specialties = new ArrayList<>();
        List<DayOfWeek> days = List.of(DayOfWeek.TUESDAY);
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
                GenderEnum.Femenino,
                specialties,
                schedule,
                false,
                new Interval( LocalTime.of(7, 0), LocalTime.of(13, 0))
        );


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
                GenderEnum.Masculino,
                0,
                history
        );

        LocalDate nextMonday = LocalDate.now().with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.TUESDAY));
        Interval interval = new Interval(LocalTime.of(7,0), LocalTime.of(13,0));

        Appointment appointment = new Appointment(doctor, nextMonday, interval, patient);

        AppointmentEntity entity = appointmentMapper.toEntity(appointment);

        Assertions.assertNotNull(entity);
        Assertions.assertNotNull(entity.getDoctor());
        Assertions.assertNotNull(entity.getPatient());
        Assertions.assertNotNull(entity.getAppointmentDate());
        Assertions.assertNotNull(entity.getAppointmentStatus());
        Assertions.assertNotNull(entity.getInterval());
        // The doctor appointments is null because for appointment is not necessary
        Assertions.assertNull(entity.getDoctor().getScheduledAppointments());
        Assertions.assertNull(entity.getDoctor().getAttendedAppointments());

        Appointment domain = appointmentMapper.toDomain(entity);

        Assertions.assertNotNull(domain);
        Assertions.assertEquals(appointment.getId(), domain.getId());
        Assertions.assertEquals(appointment.getDoctor().getId(), domain.getDoctor().getId());
        Assertions.assertEquals(appointment.getPatient().getId(), domain.getPatient().getId());
        Assertions.assertEquals(appointment.getAppointmentDate(), domain.getAppointmentDate());

    }
}
