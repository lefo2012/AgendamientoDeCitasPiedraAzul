package infrastructureTest.mappersTest;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.DocumentTypeEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.*;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.MedicalHistoryEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.MedicalHistoryMapper;
import co.edu.unicauca.BackendPiedraAzul.BackendPiedraAzulApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@SpringBootTest(classes = BackendPiedraAzulApplication.class)
public class MedicalHistoryMapperTest {

    @Autowired
    private MedicalHistoryMapper medicalHistoryMapper;

    @Test
    public void testMedicalHistoryMapper() {

        MedicalHistory history = new MedicalHistory();

        history.setId(1L);

        Map<Date, String> historyMap = new HashMap<>();
        historyMap.put(new Date(), "Patient diagnosed with lesbianismo");

        history.setMedicalHistory(historyMap);

        List<Doctor> doctors = new ArrayList<>();

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

        doctors.add(doctor);

        history.setDoctors(doctors);

        MedicalHistoryEntity entity = medicalHistoryMapper.toEntity(history);
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(history.getId(), entity.getId());
        Assertions.assertEquals(  history.getMedicalHistory().size(),  entity.getMedicalHistory().size());
        Assertions.assertEquals(  history.getDoctors().size(),  entity.getDoctors().size() );

        MedicalHistory domain = medicalHistoryMapper.toDomain(entity);
        Assertions.assertNotNull(domain);
        Assertions.assertEquals(history.getId(), domain.getId());
        Assertions.assertEquals(  history.getMedicalHistory().size(),  domain.getMedicalHistory().size());
        Assertions.assertEquals(  history.getDoctors().size(),  domain.getDoctors().size() );


    }
}
