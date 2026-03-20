package co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.AppointmentEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.AppointmentMapper;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper.MedicalHistoryMapper;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.DocumentTypeEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.dto.PatientDTO;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.PatientEntity;
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
public class PatientMapperImpl implements PatientMapper {

    @Autowired
    private MedicalHistoryMapper medicalHistoryMapper;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PatientEntity toEntity(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        PatientEntity patientEntity = new PatientEntity();

        patientEntity.setId( patient.getId() );
        patientEntity.setDocumentType( patient.getDocumentType() );
        patientEntity.setIdentificationNumber( patient.getIdentificationNumber() );
        patientEntity.setFirstName( patient.getFirstName() );
        patientEntity.setLastName( patient.getLastName() );
        patientEntity.setBirthDate( patient.getBirthDate() );
        patientEntity.setPhone( patient.getPhone() );
        patientEntity.setActive( patient.isActive() );
        patientEntity.setUser( userMapper.toEntity( patient.getUser() ) );
        patientEntity.setPendingAppointments( appointmentListToAppointmentEntityList( patient.getPendingAppointments() ) );
        patientEntity.setPastAppointments( appointmentListToAppointmentEntityList( patient.getPastAppointments() ) );
        patientEntity.setMedicalHistory( medicalHistoryMapper.toEntity( patient.getMedicalHistory() ) );
        patientEntity.setAppointmentCount( patient.getAppointmentCount() );

        return patientEntity;
    }

    @Override
    public Patient toDomain(PatientEntity patientEntity) {
        if ( patientEntity == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setId( patientEntity.getId() );
        patient.setDocumentType( patientEntity.getDocumentType() );
        patient.setIdentificationNumber( patientEntity.getIdentificationNumber() );
        patient.setFirstName( patientEntity.getFirstName() );
        patient.setLastName( patientEntity.getLastName() );
        patient.setBirthDate( patientEntity.getBirthDate() );
        patient.setPhone( patientEntity.getPhone() );
        patient.setActive( patientEntity.isActive() );
        patient.setUser( userMapper.toDomain( patientEntity.getUser() ) );
        try {
            patient.setPendingAppointments( appointmentEntityListToAppointmentList( patientEntity.getPendingAppointments() ) );
        }
        catch ( Exception e ) {
            throw new RuntimeException( e );
        }
        try {
            patient.setPastAppointments( appointmentEntityListToAppointmentList( patientEntity.getPastAppointments() ) );
        }
        catch ( Exception e ) {
            throw new RuntimeException( e );
        }
        patient.setMedicalHistory( medicalHistoryMapper.toDomain( patientEntity.getMedicalHistory() ) );
        patient.setAppointmentCount( patientEntity.getAppointmentCount() );

        return patient;
    }

    @Override
    public Patient dtoToDomain(PatientDTO patientDto) {
        if ( patientDto == null ) {
            return null;
        }

        Patient patient = new Patient();

        try {
            if ( patientDto.getBirthDate() != null ) {
                patient.setBirthDate( new SimpleDateFormat( "yyyy-MM-dd" ).parse( patientDto.getBirthDate() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        if ( patientDto.getDocumentType() != null ) {
            patient.setDocumentType( Enum.valueOf( DocumentTypeEnum.class, patientDto.getDocumentType() ) );
        }
        patient.setIdentificationNumber( patientDto.getIdentificationNumber() );
        patient.setFirstName( patientDto.getFirstName() );
        patient.setLastName( patientDto.getLastName() );
        patient.setPhone( patientDto.getPhone() );
        patient.setActive( patientDto.isActive() );
        patient.setUser( userMapper.dtoToDomain( patientDto.getUser() ) );

        return patient;
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
