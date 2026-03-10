package Appointments.domain;


import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

public class Appointment {


    private Long id;
    private Doctor doctor;
    private Patient patient;
    private LocalDate appointmentDate;
    private AppointmentStatusEnum appointmentStatus;
    private Interval interval;

    /**
     * Function that creates an appointment within the scope of the entities
     * */
    public Appointment (){

    }

     public Appointment scheduleAppointment(Doctor doctor, LocalDate appointmentDate, Interval interval, Patient patient) throws Exception {

        Appointment appointment = new Appointment(doctor, appointmentDate,interval, patient);
        doctor.addAppointmentToAttend(appointment);
        patient.addPendingAppointment(appointment);
        return appointment;
    }

    private Appointment(Doctor doctor, LocalDate appointmentDate,Interval interval, Patient patient) {
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.patient = patient;
        this.appointmentStatus = AppointmentStatusEnum.AGENDADA;
        this.interval = interval;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public AppointmentStatusEnum getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatusEnum appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }
}
