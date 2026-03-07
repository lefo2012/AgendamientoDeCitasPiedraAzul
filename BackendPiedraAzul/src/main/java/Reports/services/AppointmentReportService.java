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
     * obtains the appointments of a doctor/therapist on a specific date.
     *
     * @param doctorId ID of the doctor/therapist
     * @param appointmentDate Date of the appointments to search (format: YYYY-MM-DD)
     * @return list with appointment information for the specified doctor and date
     */
    public List<AppointmentReportDTO> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate appointmentDate) {
        //here we will implement the logic to fetch appointments from the Appointments service based on the doctorId and appointmentDate
        return List.of();
    }

    /**
     * Obtain the total count of appointments for a doctor/therapist on a specific date.
     *
     * @param doctorId ID of the doctor/therapist
     * @param appointmentDate date of the appointments to count (format: YYYY-MM-DD)
     * @return count of appointments
     */
    public long countAppointmentsByDoctorAndDate(Long doctorId, LocalDate appointmentDate) {
        //this function will implement the logic to count the total number of appointments for the specified doctor and date.
        return 0;
    }

}

