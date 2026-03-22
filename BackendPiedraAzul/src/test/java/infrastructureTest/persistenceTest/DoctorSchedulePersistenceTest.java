package infrastructureTest.persistenceTest;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Interval;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.IntervalList;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Schedule;
import co.edu.unicauca.BackendPiedraAzul.BackendPiedraAzulApplication;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.DocumentTypeEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IDoctorPersistenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BackendPiedraAzulApplication.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:doctorScheduleTestDb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class DoctorSchedulePersistenceTest {

    @Autowired
    private IDoctorPersistenceService doctorPersistenceService;

    @Test
    void shouldPersistBusyTimesWithDateWhenDoctorIsSaved() throws Exception {
        IntervalList mondayIntervals = new IntervalList();
        mondayIntervals.addInterval(new Interval(LocalTime.of(7, 0), LocalTime.of(9, 0)));

        Schedule schedule = new Schedule(
                List.of(DayOfWeek.MONDAY),
                List.of(mondayIntervals),
                8,
                LocalDate.now().getYear()
        );

        assertFalse(schedule.getAvailableTimes().isEmpty());
        LocalDate reserveDay = schedule.getAvailableTimes().keySet().stream().sorted().findFirst().orElseThrow();
        Interval reserveInterval = new Interval(LocalTime.of(7, 0), LocalTime.of(8, 0));
        assertTrue(schedule.schedule(reserveDay, reserveInterval));

        Doctor doctor = new Doctor();
        doctor.setDocumentType(DocumentTypeEnum.CC);
        doctor.setIdentificationNumber("DOC-" + System.nanoTime());
        doctor.setFirstName("Doctor");
        doctor.setLastName("BusyTime");
        doctor.setBirthDate(new Date());
        doctor.setPhone("3000000000");
        doctor.setUser(new User());
        doctor.setSpecialties(new ArrayList<>());
        doctor.setSchedule(schedule);
        doctor.setCanSchedule(true);

        Doctor savedDoctor = doctorPersistenceService.save(doctor);
        Doctor reloadedDoctor = doctorPersistenceService.findById(savedDoctor.getId());

        assertNotNull(reloadedDoctor.getSchedule());
        assertNotNull(reloadedDoctor.getSchedule().getBusyTimes());
        assertTrue(reloadedDoctor.getSchedule().getBusyTimes().containsKey(reserveDay));

        IntervalList persistedBusyDay = reloadedDoctor.getSchedule().getBusyTimes().get(reserveDay);
        assertNotNull(persistedBusyDay);
        assertEquals(reserveDay, persistedBusyDay.getDate());
        assertEquals(1, persistedBusyDay.getIntervals().size());
        assertEquals(reserveInterval, persistedBusyDay.getIntervals().get(0));
    }
}
