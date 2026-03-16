package co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Interval;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.dto.AppointmentDto;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.AppointmentMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.IntervalMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.services.persistence.AppointmentPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.DoctorPersistenceService;
import co.edu.unicauca.BackendPiedraAzul.Users.services.persistence.PatientPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void reserveApointment(AppointmentDto appointmentDto) throws Exception {


        try {
            //To do: verify the time of interval for reserve
            Doctor doctor = doctorPersistenceService.findById(appointmentDto.getIdDoctor());
            Patient patient = patientPersistenceService.findById(appointmentDto.getIdPatient());
            Interval interval = intervalMapper.dtoToDomain(appointmentDto.getInterval());
            Appointment appointment = new Appointment(doctor,appointmentDto.getAppointmentDate(),interval,patient);
            appointmentPersistenceService.save(appointment);
            doctorPersistenceService.save(doctor);
            patientPersistenceService.save(patient);

        }catch (Exception e){
            throw new Exception("Cant create appointment");
        }

    }


}
