package Reports.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for appointment report data.
 * here we encapsulate the information of an appointment that we want to include in the report.
 * This class is for to show the details of an appointment n the table.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentReportDTO {

    //posible atributes of an appointment that we want to include in the report.
    private Long appointmentId;
    private String patientName;
    private String doctorName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
}

