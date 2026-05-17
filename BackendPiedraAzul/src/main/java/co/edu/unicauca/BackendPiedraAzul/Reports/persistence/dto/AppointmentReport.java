package co.edu.unicauca.BackendPiedraAzul.Reports.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for appointment report data.
 * here we encapsulate the information of an appointment that we want to include in the report.
 * This class is for to show the details of an appointment n the table.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentReport {
    private Long id;
    private Long patientId;
    private String patientName;
    private String doctorName;
    private String appointmentInterval;
    private String date;
}

