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
    @Autowired
    private WhatsAppNotificationService whatsAppService;

    @Override
    @Transactional
    public void reserveAppointment(ReserveAppointmentDTO reserveAppointmentDto) throws Exception {
        try {
            //To do: verify the time of interval for reserve
            Doctor doctor = doctorPersistenceService.findById(reserveAppointmentDto.getIdDoctor());
            Patient patient = patientPersistenceService.findById(reserveAppointmentDto.getIdPatient());
            Interval interval = intervalMapper.dtoToDomain(reserveAppointmentDto.getInterval());
            Appointment appointment = new Appointment(doctor, reserveAppointmentDto.getAppointmentDate(), interval, patient);


            Appointment savedAppointment = appointmentPersistenceService.save(appointment);
            // Keep the generated id in the same object referenced by doctor/patient lists.
            // Without this, cascading save can treat it as a new appointment and insert duplicates.
            appointment.setId(savedAppointment.getId());

            doctorPersistenceService.save(appointment.getDoctor());
            patientPersistenceService.save(appointment.getPatient());

            whatsAppService.sendAppointmentConfirmation(
                    patient.getPhone(),
                    patient.getFirstName(),
                    doctor.getFirstName() + " " + doctor.getLastName(),
                    reserveAppointmentDto.getAppointmentDate().toString(),
                    reserveAppointmentDto.getInterval().getStartTime()
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    @Transactional
    public void reSchedule(ReserveAppointmentDTO dto) throws Exception {
        try {
            if (dto.getId() == null) {
                throw new Exception("El id de la cita a reprogramar no puede ser nulo");
            }

            Appointment oldAppointment = appointmentPersistenceService.findById(dto.getId());
            Doctor oldDoctor = doctorPersistenceService.findById(oldAppointment.getDoctor().getId());
            Patient patient = patientPersistenceService.findById(dto.getIdPatient());

            // 1. Setear estado en la cita y en la lista del doctor
            oldAppointment.setAppointmentStatus(AppointmentStatusEnum.ATENDIDA);
            oldAppointment.setDoctor(oldDoctor);

            oldDoctor.getScheduledAppointments().stream()
                    .filter(a -> a.getId().equals(dto.getId()))
                    .findFirst()
                    .ifPresent(a -> a.setAppointmentStatus(AppointmentStatusEnum.ATENDIDA));

            // 2. Mover a listas de historial
            oldDoctor.getScheduledAppointments().removeIf(a -> a.getId().equals(dto.getId()));
            oldDoctor.addAppointmentAttended(oldAppointment);
            patient.addPastAppointment(oldAppointment);
            patient.getPendingAppointments().removeIf(a -> a.getId().equals(dto.getId()));

            // 3. Guardar solo doctor y paciente — el cascade se encarga de la cita
            doctorPersistenceService.save(oldDoctor);
            patientPersistenceService.save(patient);
            // Guardar la cita por separado DESPUÉS para que el estado quede persistido
            appointmentPersistenceService.save(oldAppointment);

            // 4. Crear nueva cita
            Doctor newDoctor = doctorPersistenceService.findById(dto.getIdDoctor());
            Patient newPatient = patientPersistenceService.findById(dto.getIdPatient());
            Interval interval = intervalMapper.dtoToDomain(dto.getInterval());

            Appointment newAppointment = new Appointment(newDoctor, dto.getAppointmentDate(), interval, newPatient);
            Appointment saved = appointmentPersistenceService.save(newAppointment);
            newAppointment.setId(saved.getId());

            doctorPersistenceService.save(newAppointment.getDoctor());
            patientPersistenceService.save(newAppointment.getPatient());

            whatsAppService.sendAppointmentReschedule(
                    newPatient.getPhone(),
                    newPatient.getFirstName(),
                    newDoctor.getFirstName() + " " + newDoctor.getLastName(),
                    dto.getAppointmentDate().toString(),
                    dto.getInterval().getStartTime()
            );

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

            Doctor doctor = doctorPersistenceService.findById(appointment.getDoctor().getId());
            Patient patient = appointment.getPatient();
            appointment.setAppointmentStatus(AppointmentStatusEnum.CANCELADA);

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
            whatsAppService.sendAppointmentCancellation(
                    patient.getPhone(),
                    patient.getFirstName(),
                    appointment.getAppointmentDate().toString(),
                    appointment.getInterval().getStartTime().toString()
            );

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
    public List<Appointment> getAllAppointments() throws Exception {

        List<Appointment> appointments = this.appointmentPersistenceService.findAll();
        return appointments;
    }

    @Override
    public List<Appointment> getAllAppointmentsByDate(LocalDate date) throws Exception {
        List<Appointment> appointments = this.appointmentPersistenceService.findAll();
        return appointments.stream()
                .filter(app -> app.getAppointmentDate().isEqual(date))
                .toList();
    }
}
