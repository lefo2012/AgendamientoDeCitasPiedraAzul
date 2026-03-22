package infrastructureTest.mappersTest;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.DocumentTypeEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.*;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.PatientEntity;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper.PatientMapper;
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
public class PatientMapperTest {

    @Autowired
    private PatientMapper patientMapper;

    @Test
    public void patientMapperTest() throws Exception {

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
                schedule,
                false,
                new Interval( LocalTime.of(7, 0), LocalTime.of(13, 0))
        );


        MedicalHistory history = new MedicalHistory();

        Patient patient = new Patient(
                2L,
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

        LocalDate nextMonday = LocalDate.now().with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));

        Interval interval = new Interval(LocalTime.of(7,0), LocalTime.of(8,0));

        Appointment appointment = new Appointment(doctor, nextMonday, interval, patient);

        PatientEntity entity = patientMapper.toEntity(patient);

        Assertions.assertNotNull(entity);
        Assertions.assertEquals(2L,entity.getId());
        Assertions.assertNotNull(entity.getPendingAppointments());
        Assertions.assertEquals(1,entity.getPendingAppointments().size());
        Assertions.assertNotNull(entity.getPastAppointments());
        Assertions.assertEquals(0,entity.getPastAppointments().size());

        Assertions.assertEquals(2L,entity.getPendingAppointments().getFirst().getPatient().getId());
        Assertions.assertEquals(1L,entity.getPendingAppointments().getFirst().getDoctor().getId());

        Patient domain = patientMapper.toDomain(entity);

        Assertions.assertNotNull(domain);
        Assertions.assertEquals(patient.getId(),domain.getId());
        Assertions.assertNotNull(domain.getPendingAppointments());
        Assertions.assertEquals(patient.getPendingAppointments().size(),domain.getPendingAppointments().size());
        Assertions.assertNotNull(domain.getPastAppointments());
        Assertions.assertEquals(patient.getPastAppointments().size(),domain.getPastAppointments().size());

        Assertions.assertEquals(patient.getPendingAppointments().getFirst().getPatient().getId(),domain.getPendingAppointments().getFirst().getPatient().getId());
        Assertions.assertEquals(patient.getPendingAppointments().getFirst().getDoctor().getId(),domain.getPendingAppointments().getFirst().getDoctor().getId());

    }
}
