package Appointments.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Doctor extends Person{

    private List<SpecialtyEnum> specialties;

    private List<Appointment> scheduledAppointments;

    private List<Appointment> attendedAppointments;

    //Think about a value object to make date distribution easier
    private Schedule schedule;

    public Doctor() {
        this.specialties = new ArrayList<>();
        this.scheduledAppointments = new ArrayList<>();
        this.attendedAppointments = new ArrayList<>();
    }

    //constructor with all parameters, except lists, which are initialized as empty
    public Doctor(long id, DocumentTypeEnum idType, String identificationNumber, String firstName, String lastName, Date birthDate, String phone, User user , List<SpecialtyEnum> specialties, Schedule schedule) {
        super(id, idType, identificationNumber, firstName, lastName, birthDate,  phone, true, user );
        this.specialties = specialties;
        this.scheduledAppointments = new ArrayList<>();
        this.attendedAppointments = new ArrayList<>();
        this.schedule = schedule;
    }

    //constructor with all parameters
    public Doctor(long id, DocumentTypeEnum idType, String identificationNumber, String firstName, String lastName, Date birthDate, String phone, User user , List<SpecialtyEnum> specialties, List<Appointment> scheduledAppointments, List<Appointment> attendedAppointments, Schedule schedule) {
        super(id, idType, identificationNumber, firstName, lastName, birthDate,  phone, true, user );
        this.specialties = specialties;
        this.scheduledAppointments = scheduledAppointments;
        this.attendedAppointments = attendedAppointments;
        this.schedule = schedule;
    }

    public boolean addAppointmentToAttend(Appointment appointment) throws Exception {

        schedule.schedule(appointment.getAppointmentDate(),appointment.getInterval());

        if (scheduledAppointments == null) {
            scheduledAppointments = new ArrayList<>();
        }
        scheduledAppointments.add(appointment);

        return true;

    }

    public boolean cancelAppointment(Appointment appointment) {

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
}
