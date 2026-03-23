package servicesTest;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.*;
import co.edu.unicauca.BackendPiedraAzul.BackendPiedraAzulApplication;
import co.edu.unicauca.BackendPiedraAzul.Reports.persistence.dto.AppointmentReport;
import co.edu.unicauca.BackendPiedraAzul.Reports.services.usecases.IAppointmentReportService;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = BackendPiedraAzulApplication.class)
public class AppointmentReportServiceTest {

    @Autowired
    IAppointmentReportService reportService;

    @Test
    void shouldConvertAppointmentsToReportDTO() throws Exception {

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

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);

        List<AppointmentReport> result = reportService.convertInAppointmentReportDTO(appointments);

        assertEquals(1, result.size());

        AppointmentReport report = result.getFirst();

        assertEquals("Angela Mia", report.getDoctorName());
        assertEquals("Lu Fer", report.getPatientName());
        assertEquals("07:00 - 13:00", report.getAppointmentInterval());
    }

}
