package co.edu.unicauca.BackendPiedraAzul.Appointments.domain;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicalHistory {
    private Long id;
    private Map<Date, String> medicalHistory;

    public MedicalHistory(){
        this.medicalHistory=new HashMap<>();
    }

    public MedicalHistory(Long id, Map<Date, String> medicalHistory, List<Doctor> doctors) {
        this.id = id;
        this.medicalHistory = medicalHistory;
        this.doctors = doctors;
    }

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
