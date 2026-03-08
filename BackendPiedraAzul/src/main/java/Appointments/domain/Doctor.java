package Appointments.domain;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends Person{

    private List<SpecialtyEnum> specialties;

    private List<Appointment> scheduledAppointments;

    private List<Appointment> attendedAppointments;

    //Think about a value object to make date distribution easier
    private Schedule schedule;

    public boolean addAppointmentToAttend(Appointment appointment) {
        if (scheduledAppointments == null) {
            scheduledAppointments = new ArrayList<>();
        }
        scheduledAppointments.add(appointment);
        return true;

    }

    public List<SpecialtyEnum> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<SpecialtyEnum> specialties) {
        this.specialties = specialties;
    }

    public List<Appointment> getScheduledAppointments() {
        return scheduledAppointments;
    }

    public void setScheduledAppointments(List<Appointment> scheduledAppointments) {
        this.scheduledAppointments = scheduledAppointments;
    }

    public List<Appointment> getAttendedAppointments() {
        return attendedAppointments;
    }

    public void setAttendedAppointments(List<Appointment> attendedAppointments) {
        this.attendedAppointments = attendedAppointments;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
