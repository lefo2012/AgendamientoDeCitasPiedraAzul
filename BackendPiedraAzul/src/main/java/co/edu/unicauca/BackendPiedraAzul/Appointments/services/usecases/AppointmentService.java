package co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.AppointmentStatusEnum;
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

import java.time.LocalDate;
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

            
            Appointment savedAppointment = appointmentPersistenceService.save(appointment);
            // Keep the generated id in the same object referenced by doctor/patient lists.
            // Without this, cascading save can treat it as a new appointment and insert duplicates.
            appointment.setId(savedAppointment.getId());

            doctorPersistenceService.save(appointment.getDoctor());
            patientPersistenceService.save(appointment.getPatient());

        }catch (Exception e){
          e.printStackTrace();
          throw e;
        }
    }


    @Override
    @Transactional
    public void reSchedule(ReserveAppointmentDTO dto) throws Exception {
        try {
            // 1. Marcar la cita anterior como ATENDIDA
            if (dto.getId() != null) {
                Appointment oldAppointment = appointmentPersistenceService.findById(dto.getId());
                Doctor oldDoctor = doctorPersistenceService.findById(oldAppointment.getDoctor().getId());
                Patient patient = patientPersistenceService.findById(dto.getIdPatient());

                oldAppointment.setAppointmentStatus(AppointmentStatusEnum.ATENDIDA);
                oldAppointment.setDoctor(oldDoctor);
                oldDoctor.cancelAppointment(oldAppointment);

                oldDoctor.getScheduledAppointments().stream()
                        .filter(a -> a.getId().equals(dto.getId()))
                        .findFirst()
                        .ifPresent(a -> a.setAppointmentStatus(AppointmentStatusEnum.ATENDIDA));

                // Agregar a listas de historial
                oldDoctor.addAppointmentAttended(oldAppointment);
                patient.addPastAppointment(oldAppointment);
                patient.getPendingAppointments().removeIf(a -> a.getId().equals(dto.getId()));

                // Guardar la cita vieja primero para que tenga estado ATENDIDA persistido
                appointmentPersistenceService.save(oldAppointment);
                doctorPersistenceService.save(oldDoctor);
                patientPersistenceService.save(patient);
            }

            // 2. Crear nueva cita — el constructor ya llama addAppointmentToAttend y addPendingAppointment
            Doctor newDoctor = doctorPersistenceService.findById(dto.getIdDoctor());
            Patient newPatient = patientPersistenceService.findById(dto.getIdPatient());
            Interval interval = intervalMapper.dtoToDomain(dto.getInterval());

            Appointment newAppointment = new Appointment(newDoctor, dto.getAppointmentDate(), interval, newPatient);
            Appointment saved = appointmentPersistenceService.save(newAppointment);
            newAppointment.setId(saved.getId());

            doctorPersistenceService.save(newAppointment.getDoctor());
            patientPersistenceService.save(newAppointment.getPatient());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional
    public void cancelAppointment(Long appointmentId) throws Exception {
        try {
            Appointment appointment = appointmentPersistenceService.findById(appointmentId);
            System.out.println("Estado antes: " + appointment.getAppointmentStatus());

            Doctor doctor = doctorPersistenceService.findById(appointment.getDoctor().getId());
            Patient patient = appointment.getPatient();

            appointment.setAppointmentStatus(AppointmentStatusEnum.CANCELADA);
            System.out.println("Estado después de setear: " + appointment.getAppointmentStatus());

            appointment.setDoctor(doctor);
            doctor.cancelAppointment(appointment);
            patient.getPendingAppointments().removeIf(a -> a.getId().equals(appointmentId));

            doctor.getScheduledAppointments().stream()
                    .filter(a -> a.getId().equals(appointmentId))
                    .findFirst()
                    .ifPresent(a -> a.setAppointmentStatus(AppointmentStatusEnum.CANCELADA));

            Appointment saved = appointmentPersistenceService.save(appointment);
            doctorPersistenceService.save(doctor);
            patientPersistenceService.save(patient);
            System.out.println("Estado guardado: " + saved.getAppointmentStatus());
            System.out.println("estado del doctor guardado: " + doctor.getScheduledAppointments().getFirst().getAppointmentStatus());

        } catch (Exception e) {
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

    @Override
    public List<Appointment> getAllAppointments() throws Exception{

         List<Appointment> appointments = this.appointmentPersistenceService.findAll();
         return appointments;
    }

    @Override
    public List<Appointment> getAllAppointmentsByDate(LocalDate date)throws Exception{
        List<Appointment> appointments = this.appointmentPersistenceService.findAll();
        return appointments.stream()
                .filter(app -> app.getAppointmentDate().isEqual(date))
                .toList();
    }
}
