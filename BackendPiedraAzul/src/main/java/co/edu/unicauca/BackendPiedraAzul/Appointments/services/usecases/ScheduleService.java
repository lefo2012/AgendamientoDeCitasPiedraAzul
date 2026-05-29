package co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.IntervalList;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Schedule;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ConfigureScheduleDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.IntervalListDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.IntervalListMapper;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IDoctorPersistenceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService implements IScheduleService {
    private final IDoctorPersistenceService doctorPersistenceService;

    private final IntervalListMapper intervalListMapper;

    public ScheduleService(IDoctorPersistenceService doctorPersistenceService, IntervalListMapper intervalListMapper) {
        this.doctorPersistenceService = doctorPersistenceService;
        this.intervalListMapper = intervalListMapper;
    }

    @Override
    @Transactional
    public boolean configureSchedule(Long doctorId, ConfigureScheduleDTO dto) {
        try {
            Doctor doctor = doctorPersistenceService.findById(doctorId);

            List<IntervalListDTO> intervalsDTO = dto.getSchedules();

            List<IntervalList> intervals = intervalsDTO.stream().map(intervalListMapper::dtoToDomain).toList();

            Schedule schedule = new Schedule(
                    dto.getDays(),
                    intervals,
                    dto.getWeeksRepeat(),
                    dto.getYear()
            );

            doctor.setSchedule(schedule);
            doctor.setActive(true);
            doctorPersistenceService.save(doctor);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
