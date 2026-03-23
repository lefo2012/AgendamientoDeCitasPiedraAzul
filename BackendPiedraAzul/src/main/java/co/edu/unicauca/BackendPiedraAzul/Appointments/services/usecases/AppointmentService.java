package co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Interval;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.AppointmentDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.ReserveAppointmentDTO;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.AppointmentMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.IntervalMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence.IAppointmentPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IDoctorPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.IPatientPersistenceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService implements IAppointmentService {

    @Autowired
    private IAppointmentPersistenceService appointmentPersistenceService;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private IDoctorPersistenceService doctorPersistenceService;
    @Autowired
    private IPatientPersistenceService patientPersistenceService;
    @Autowired
    private IntervalMapper intervalMapper;

    @Override
    @Transactional
    public void reserveAppointment(ReserveAppointmentDTO reserveAppointmentDto) throws Exception {
        try {
            //To do: verify the time of interval for reserve
            Doctor doctor = doctorPersistenceService.findById(reserveAppointmentDto.getIdDoctor());
            Patient patient = patientPersistenceService.findById(reserveAppointmentDto.getIdPatient());
            Interval interval = intervalMapper.dtoToDomain(reserveAppointmentDto.getInterval());
            Appointment appointment = new Appointment(doctor, reserveAppointmentDto.getAppointmentDate(),interval,patient);

            appointmentPersistenceService.save(appointment);
            doctor.getSchedule().print();
            System.out.println("---------------------------------------------------------------------------------------------");
            appointment.getDoctor().getSchedule().print();

            doctorPersistenceService.save(appointment.getDoctor());

            patientPersistenceService.save(appointment.getPatient());

        }catch (Exception e){
          e.printStackTrace();
          throw e;
        }
    }

    @Override
    public List<AppointmentDTO> getScheduledAppointmentsByDoctor(Long doctorId) throws Exception {

        Doctor doctor = doctorPersistenceService.findById(doctorId);

        return doctor.getScheduledAppointments().stream().map(appointmentMapper::toDto).toList();

    }

    @Override
    public List<AppointmentDTO> getAttendedAppointments(Long doctorId) throws Exception {
        Doctor doctor = doctorPersistenceService.findById(doctorId);
        return doctor.getAttendedAppointments().stream().map(appointmentMapper::toDto).toList();
    }

}
