package Reports.services;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for handling appointment report logic.
 * this class will contain methods to retrieve and process appointment data for generating reports.
 * the methods in this class will interact with the Appointments service to fetch the necessary data
 */
public class AppointmentReportService {

    /**
     * Obtain the total count of appointments for a doctor/therapist on a specific date.
     *
     * @param doctorId ID of the doctor/therapist
     * @param appointmentDate date of the appointments to count (format: YYYY-MM-DD)
     * @return count of appointments
     */
    public long countAppointmentsByDoctorAndDate(Long doctorId, LocalDate appointmentDate) {
        //no se si esto va aqui
        return 0;
    }

}

