package co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.SpecialtyEnum;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.AppointmentEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.AppointmentMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.ScheduleMapper;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.DocumentTypeEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.DoctorDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.DoctorEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-14T19:52:29-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class DoctorMapperImpl implements DoctorMapper {

    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public DoctorEntity toEntity(Doctor doctor) {
        if ( doctor == null ) {
            return null;
        }

        DoctorEntity doctorEntity = new DoctorEntity();

        doctorEntity.setId( doctor.getId() );
        doctorEntity.setDocumentType( doctor.getDocumentType() );
        doctorEntity.setIdentificationNumber( doctor.getIdentificationNumber() );
        doctorEntity.setFirstName( doctor.getFirstName() );
        doctorEntity.setLastName( doctor.getLastName() );
        doctorEntity.setBirthDate( doctor.getBirthDate() );
        doctorEntity.setPhone( doctor.getPhone() );
        doctorEntity.setActive( doctor.isActive() );
        doctorEntity.setUser( userMapper.toEntity( doctor.getUser() ) );
        List<SpecialtyEnum> list = doctor.getSpecialties();
        if ( list != null ) {
            doctorEntity.setSpecialties( new ArrayList<SpecialtyEnum>( list ) );
        }
        doctorEntity.setScheduledAppointments( appointmentListToAppointmentEntityList( doctor.getScheduledAppointments() ) );
        doctorEntity.setAttendedAppointments( appointmentListToAppointmentEntityList( doctor.getAttendedAppointments() ) );
        doctorEntity.setSchedule( scheduleMapper.toEntity( doctor.getSchedule() ) );

        return doctorEntity;
    }

    @Override
    public Doctor toDomain(DoctorEntity doctorEntity) {
        if ( doctorEntity == null ) {
            return null;
        }

        Doctor doctor = new Doctor();

        doctor.setId( doctorEntity.getId() );
        doctor.setDocumentType( doctorEntity.getDocumentType() );
        doctor.setIdentificationNumber( doctorEntity.getIdentificationNumber() );
        doctor.setFirstName( doctorEntity.getFirstName() );
        doctor.setLastName( doctorEntity.getLastName() );
        doctor.setBirthDate( doctorEntity.getBirthDate() );
        doctor.setPhone( doctorEntity.getPhone() );
        doctor.setActive( doctorEntity.isActive() );
        doctor.setUser( userMapper.toDomain( doctorEntity.getUser() ) );
        List<SpecialtyEnum> list = doctorEntity.getSpecialties();
        if ( list != null ) {
            doctor.setSpecialties( new ArrayList<SpecialtyEnum>( list ) );
        }
        try {
            doctor.setScheduledAppointments( appointmentEntityListToAppointmentList( doctorEntity.getScheduledAppointments() ) );
        }
        catch ( Exception e ) {
            throw new RuntimeException( e );
        }
        try {
            doctor.setAttendedAppointments( appointmentEntityListToAppointmentList( doctorEntity.getAttendedAppointments() ) );
        }
        catch ( Exception e ) {
            throw new RuntimeException( e );
        }
        doctor.setSchedule( scheduleMapper.toDomain( doctorEntity.getSchedule() ) );

        return doctor;
    }

    @Override
    public Doctor dtoToDomain(DoctorDTO doctorDto) {
        if ( doctorDto == null ) {
            return null;
        }

        Doctor doctor = new Doctor();

        try {
            if ( doctorDto.getBirthDate() != null ) {
                doctor.setBirthDate( new SimpleDateFormat( "yyyy-MM-dd" ).parse( doctorDto.getBirthDate() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        if ( doctorDto.getDocumentType() != null ) {
            doctor.setDocumentType( Enum.valueOf( DocumentTypeEnum.class, doctorDto.getDocumentType() ) );
        }
        doctor.setIdentificationNumber( doctorDto.getIdentificationNumber() );
        doctor.setFirstName( doctorDto.getFirstName() );
        doctor.setLastName( doctorDto.getLastName() );
        doctor.setPhone( doctorDto.getPhone() );
        doctor.setActive( doctorDto.isActive() );
        doctor.setUser( userMapper.dtoToDomain( doctorDto.getUser() ) );
        List<SpecialtyEnum> list = doctorDto.getSpecialties();
        if ( list != null ) {
            doctor.setSpecialties( new ArrayList<SpecialtyEnum>( list ) );
        }

        return doctor;
    }

    protected List<AppointmentEntity> appointmentListToAppointmentEntityList(List<Appointment> list) {
        if ( list == null ) {
            return null;
        }

        List<AppointmentEntity> list1 = new ArrayList<AppointmentEntity>( list.size() );
        for ( Appointment appointment : list ) {
            list1.add( appointmentMapper.toEntity( appointment ) );
        }

        return list1;
    }

    protected List<Appointment> appointmentEntityListToAppointmentList(List<AppointmentEntity> list) throws Exception {
        if ( list == null ) {
            return null;
        }

        List<Appointment> list1 = new ArrayList<Appointment>( list.size() );
        for ( AppointmentEntity appointmentEntity : list ) {
            list1.add( appointmentMapper.toDomain( appointmentEntity ) );
        }

        return list1;
    }
}
