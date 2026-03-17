package co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Interval;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.AppointmentDto;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ReserveAppointmentDto;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.AppointmentMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.IntervalMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence.AppointmentPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.DoctorPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.PatientPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService implements IAppointmentService {

    @Autowired
    private AppointmentPersistenceService appointmentPersistenceService;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private DoctorPersistenceService doctorPersistenceService;
    @Autowired
    private PatientPersistenceService patientPersistenceService;
    @Autowired
    private IntervalMapper intervalMapper;


    public void reserveApointment(ReserveAppointmentDto reserveAppointmentDto) throws Exception {
        try {
            //To do: verify the time of interval for reserve
            Doctor doctor = doctorPersistenceService.findById(reserveAppointmentDto.getIdDoctor());
            Patient patient = patientPersistenceService.findById(reserveAppointmentDto.getIdPatient());
            Interval interval = intervalMapper.dtoToDomain(reserveAppointmentDto.getInterval());
            Appointment appointment = new Appointment(doctor, reserveAppointmentDto.getAppointmentDate(),interval,patient);
            appointmentPersistenceService.save(appointment);
            doctorPersistenceService.save(doctor);
            patientPersistenceService.save(patient);

        }catch (Exception e){
            throw new Exception("Cant create appointment");
        }
    }
    public List<AppointmentDto> getScheduledAppointmentsByDoctor(Long doctorId) throws Exception {

        Doctor doctor = doctorPersistenceService.findById(doctorId);

        return doctor.getScheduledAppointments().stream().map(appointmentMapper::toDto).toList();

    }

    public List<AppointmentDto> getAttendedAppointments(Long patientId) throws Exception {
        Doctor doctor = doctorPersistenceService.findById(patientId);
        return doctor.getAttendedAppointments().stream().map(appointmentMapper::toDto).toList();
    }

}
