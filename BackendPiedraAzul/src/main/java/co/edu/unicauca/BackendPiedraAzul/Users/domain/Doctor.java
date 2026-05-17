package co.edu.unicauca.BackendPiedraAzul.Users.domain;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Doctor extends Person {

    private List<SpecialtyEnum> specialties;

    private List<Appointment> scheduledAppointments;

    private List<Appointment> attendedAppointments;

    //Think about a value object to make date distribution easier
    private Schedule schedule;

    private boolean canSchedule;

    private Interval appointmentInterval;

    public Doctor() {
        super();
        this.canSchedule= false;
        this.specialties = new ArrayList<>();
        this.scheduledAppointments = new ArrayList<>();
        this.attendedAppointments = new ArrayList<>();
    }

    //constructor with all parameters, except lists, which are initialized as empty
    public Doctor(long id, DocumentTypeEnum idType, String identificationNumber, String firstName, String lastName, Date birthDate, String phone, User user , GenderEnum gender , List<SpecialtyEnum> specialties, Schedule schedule, boolean canSchedule, Interval appointmentInterval) {
        super(id, idType, identificationNumber, firstName, lastName, birthDate,  phone, true, user , gender);
        this.specialties = specialties;
        this.scheduledAppointments = new ArrayList<>();
        this.attendedAppointments = new ArrayList<>();
        this.schedule = schedule;
        this.canSchedule = canSchedule;
        this.appointmentInterval = appointmentInterval;
    }

    //constructor with all parameters
    public Doctor(long id, DocumentTypeEnum idType, String identificationNumber, String firstName, String lastName, Date birthDate, String phone, User user , GenderEnum gender, List<SpecialtyEnum> specialties, List<Appointment> scheduledAppointments, List<Appointment> attendedAppointments, Schedule schedule, boolean canSchedule, Interval appointmentInterval) {
        super(id, idType, identificationNumber, firstName, lastName, birthDate,  phone, true, user , gender);
        this.specialties = specialties;
        this.scheduledAppointments = scheduledAppointments;
        this.attendedAppointments = attendedAppointments;
        this.schedule = schedule;
        this.canSchedule = canSchedule;
        this.appointmentInterval = appointmentInterval;
    }

    public boolean addAppointmentToAttend(Appointment appointment) throws Exception {

        schedule.schedule(appointment.getAppointmentDate(),appointment.getInterval());

        if (scheduledAppointments == null) {
            scheduledAppointments = new ArrayList<>();
        }
        scheduledAppointments.add(appointment);

        return true;

    }

    public boolean addAppointmentAttended(Appointment appointment) throws Exception {

        if (attendedAppointments == null) {
            attendedAppointments = new ArrayList<>();
        }
        attendedAppointments.add(appointment);

        return true;

    }

    public boolean cancelAppointment(Appointment appointment) {

        schedule.cancel(appointment.getAppointmentDate(),appointment.getInterval());

        return scheduledAppointments.remove(appointment);

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

    public boolean isCanSchedule() {
        return canSchedule;
    }
    public void setCanSchedule(boolean canSchedule) {
        this.canSchedule = canSchedule;
    }

    public Interval getAppointmentInterval() {
        return appointmentInterval;
    }

    public void setAppointmentInterval(Interval appointmentInterval) {
        this.appointmentInterval = appointmentInterval;
    }
}
