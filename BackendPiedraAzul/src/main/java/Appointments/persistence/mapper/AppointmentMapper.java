package Appointments.persistence.mapper;

import Appointments.domain.Appointment;
import Appointments.persistence.entities.AppointmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class, PatientMapper.class})
public interface AppointmentMapper {
        Appointment toAppointment(AppointmentEntity appointmentEntity);
        AppointmentEntity toAppointmentEntity(Appointment appointment);
}
