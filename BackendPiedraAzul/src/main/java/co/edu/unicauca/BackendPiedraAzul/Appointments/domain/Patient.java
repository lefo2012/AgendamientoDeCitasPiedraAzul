package co.edu.unicauca.BackendPiedraAzul.Appointments.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient extends Person{

//    private List<Appointment> pendingAppointments;
//    private List<Appointment> pastAppointments;
    private MedicalHistory medicalHistory;
    private int appointmentCount;

    public Patient() {
        super();
    }

    public Patient(Long id, DocumentTypeEnum documentType, String identificationNumber, String firstName, String lastName,
                   Date birthDate, String phone, boolean active, User user, List<Appointment> pendingAppointments,
                   int appointmentCount, MedicalHistory medicalHistory, List<Appointment> pastAppointments) {
        super(id, documentType, identificationNumber, firstName, lastName, birthDate, phone, active, user);
//        this.pendingAppointments = pendingAppointments;
        this.appointmentCount = appointmentCount;
        this.medicalHistory = medicalHistory;
//        this.pastAppointments = pastAppointments;
    }

    public boolean addPendingAppointment(Appointment appointment) {

//        if (pendingAppointments == null) {
//            pendingAppointments = new ArrayList<>();
//        }
//        pendingAppointments.add(appointment);
        appointmentCount++;

        return true;
    }

//    public List<Appointment> getPendingAppointments() {
//        return pendingAppointments;
//    }
//
//    public void setPendingAppointments(List<Appointment> pendingAppointments) {
//        this.pendingAppointments = pendingAppointments;
//    }
//
//    public List<Appointment> getPastAppointments() {
//        return pastAppointments;
//    }
//
//    public void setPastAppointments(List<Appointment> pastAppointments) {
//        this.pastAppointments = pastAppointments;
//    }

    public MedicalHistory getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(MedicalHistory medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public int getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(int appointmentCount) {
        this.appointmentCount = appointmentCount;
    }
}
