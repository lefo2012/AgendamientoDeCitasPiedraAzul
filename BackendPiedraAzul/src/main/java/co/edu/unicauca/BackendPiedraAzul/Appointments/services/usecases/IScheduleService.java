package co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ConfigureScheduleDTO;

public interface IScheduleService {
    boolean configureSchedule(Long doctorId, ConfigureScheduleDTO dto);
}
