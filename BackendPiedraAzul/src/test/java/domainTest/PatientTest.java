package domainTest;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PatientTest {

    /**
     * Test constructor with all parameters
     */
    @Test
    void constructorWithAllParametersTest() {
        User user = new User();
        List<Appointment> pending = new ArrayList<>();
        List<Appointment> past = new ArrayList<>();
        MedicalHistory history = new MedicalHistory();

        Patient patient = new Patient(
                1L,
                DocumentTypeEnum.CC,
                "987654321",
                "Lu",
                "Fer",
                new Date(),
                "3105551234",
                true,
                user,
                pending,
                0,
                history,
                past
        );

        assertEquals(user, patient.getUser());
//        assertEquals(pending, patient.getPendingAppointments());
//        assertEquals(past, patient.getPastAppointments());
        assertEquals(history, patient.getMedicalHistory());
        assertEquals(0, patient.getAppointmentCount());
    }

    /**
     * Test addPendingAppointment add appointments correctly and updates appointment count
     */
    @Test
    void addPendingAppointmentTest() {
        Patient patient = new Patient();
        Appointment appointment = new Appointment();

        // the list is null at the beginning, but the method should initialize it
//        assertNull(patient.getPendingAppointments());

        boolean added = patient.addPendingAppointment(appointment);

        assertTrue(added);
//        assertNotNull(patient.getPendingAppointments());
//        assertEquals(1, patient.getPendingAppointments().size());
//        assertTrue(patient.getPendingAppointments().contains(appointment));
        assertEquals(1, patient.getAppointmentCount());

        // Add another appointment
        Appointment appointment2 = new Appointment();
        patient.addPendingAppointment(appointment2);
//
//        assertEquals(2, patient.getPendingAppointments().size());
        assertEquals(2, patient.getAppointmentCount());
//        assertTrue(patient.getPendingAppointments().contains(appointment2));
    }

}