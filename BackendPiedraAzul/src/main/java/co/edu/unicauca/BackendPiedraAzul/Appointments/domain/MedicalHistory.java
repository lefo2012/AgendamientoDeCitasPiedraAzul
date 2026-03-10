package co.edu.unicauca.BackendPiedraAzul.Appointments.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MedicalHistory {
    private Long id;

    private Map<Date, String> medicalHistory;
    //We need to look at this carefully if it's interesting or if we can leave it just in the history as such

    private List<Doctor> doctors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Date, String> getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(Map<Date, String> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }
}
