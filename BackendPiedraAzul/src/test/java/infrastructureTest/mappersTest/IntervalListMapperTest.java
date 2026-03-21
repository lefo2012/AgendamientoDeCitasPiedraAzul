package infrastructureTest.mappersTest;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Interval;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.IntervalList;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.IntervalListEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.IntervalListMapper;
import co.edu.unicauca.BackendPiedraAzul.BackendPiedraAzulApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest(classes = BackendPiedraAzulApplication.class)
public class IntervalListMapperTest {

    @Autowired
    private IntervalListMapper intervalListMapper;

    @Test
    public void intervalListMapperTest(){

        IntervalList intervalList = new IntervalList();

        intervalList.setId(1L);
        intervalList.setDate(LocalDate.now());

        Interval interval1 = new Interval(LocalTime.of(7,0), LocalTime.of(8,0));
        Interval interval2 = new Interval(LocalTime.of(8,0), LocalTime.of(9,0));

        intervalList.addInterval(interval1);
        intervalList.addInterval(interval2);

        IntervalListEntity entity = intervalListMapper.toEntity(intervalList);

        Assertions.assertNotNull(entity);
        Assertions.assertEquals(intervalList.getId(), entity.getId());
        Assertions.assertEquals(intervalList.getIntervals().size(),entity.getIntervals().size());
        Assertions.assertEquals( intervalList.getDate(),entity.getDate() );

        IntervalList domain = intervalListMapper.toDomain(entity);

        Assertions.assertNotNull(domain);
        Assertions.assertEquals(intervalList.getId(), domain.getId());
        Assertions.assertEquals(intervalList.getIntervals().size(),domain.getIntervals().size());
        Assertions.assertEquals(intervalList.getDate(),domain.getDate() );

    }
}
