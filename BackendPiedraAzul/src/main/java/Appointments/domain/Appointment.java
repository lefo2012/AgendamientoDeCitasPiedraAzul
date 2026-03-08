package Appointments.domain;

import Appointments.domain.*;

import java.util.Date;

public class Appointment {

    private Long id;
    private Doctor doctor;
    private Patient patient;
    private Date appointmentDate;
    private AppointmentStatusEnum appointmentStatus;

    /*
     * Function that creates an appointment within the scope of the entities
     * */
    Appointment scheduleAppointment(Doctor doctor, Date appointmentDate, Patient patient) {

        Appointment appointment = new Appointment(doctor, appointmentDate, patient);

        doctor.addAppointmentToAttend(appointment);

        patient.addPendingAppointment(appointment);

        return appointment;
    }

    private Appointment(Doctor doctor, Date appointmentDate, Patient patient) {
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.patient = patient;
        this.appointmentStatus = AppointmentStatusEnum.EN_PROCESO;
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

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public AppointmentStatusEnum getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatusEnum appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }
}
