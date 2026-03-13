package co.edu.unicauca.BackendPiedraAzul.Appointments.domain;


import java.time.LocalDate;

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
    private Appointment (){

    }

    public Appointment(Doctor doctor, LocalDate appointmentDate,Interval interval, Patient patient) throws Exception {

        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDate = appointmentDate;
        this.appointmentStatus = AppointmentStatusEnum.AGENDADA;
        this.interval = interval;

        doctor.addAppointmentToAttend(this);
        patient.addPendingAppointment(this);
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
