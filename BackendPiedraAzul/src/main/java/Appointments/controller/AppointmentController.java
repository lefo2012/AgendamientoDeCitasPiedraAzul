package Appointments.controller;

import Appointments.domain.*;
import Appointments.services.persistence.AppointmentPersistenceService;
import Appointments.services.persistence.IAppointmentPersistenceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final IAppointmentPersistenceService service;

    public AppointmentController(IAppointmentPersistenceService service) {
        this.service = service;
    }

    @GetMapping("/test")
    public Appointment createTestAppointment() {
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

        Patient patient = new Patient();
        patient.setPendingAppointments(new ArrayList<>());

        System.out.println("entramoooooooo");
        LocalDate nextMonday = LocalDate.now().with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));
        Interval interval = new Interval(LocalTime.of(7,0), LocalTime.of(8,0));
        try{
            Appointment appointment = new Appointment().scheduleAppointment(doctor, nextMonday, interval, patient);
            service.save(appointment);
            return appointment;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }}
