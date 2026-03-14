package co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.MedicalHistory;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IMedicalHistoryPersistenceService {

    @Transactional
    MedicalHistory save(MedicalHistory medicalHistory);

    @Transactional
    List<MedicalHistory> findAll();

}
