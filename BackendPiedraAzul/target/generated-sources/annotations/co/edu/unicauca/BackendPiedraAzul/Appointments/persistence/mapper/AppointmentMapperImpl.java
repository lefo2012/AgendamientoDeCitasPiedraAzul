package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.Appointment;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.MedicalHistory;
import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.SpecialtyEnum;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.AppointmentEntity;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.MedicalHistoryEntity;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Patient;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Role;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.User;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.DoctorEntity;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.PatientEntity;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.RoleEntity;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.UserEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-14T19:52:29-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class AppointmentMapperImpl implements AppointmentMapper {

    @Autowired
    private IntervalMapper intervalMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public Appointment toDomain(AppointmentEntity appointmentEntity) throws Exception {
        if ( appointmentEntity == null ) {
            return null;
        }

        Appointment appointment = new Appointment();

        appointment.setId( appointmentEntity.getId() );
        appointment.setDoctor( doctorEntityToDoctor( appointmentEntity.getDoctor() ) );
        appointment.setPatient( patientEntityToPatient( appointmentEntity.getPatient() ) );
        appointment.setAppointmentDate( appointmentEntity.getAppointmentDate() );
        appointment.setAppointmentStatus( appointmentEntity.getAppointmentStatus() );
        appointment.setInterval( intervalMapper.toDomain( appointmentEntity.getInterval() ) );

        return appointment;
    }

    @Override
    public AppointmentEntity toEntity(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }

        AppointmentEntity appointmentEntity = new AppointmentEntity();

        appointmentEntity.setId( appointment.getId() );
        appointmentEntity.setDoctor( doctorToDoctorEntity( appointment.getDoctor() ) );
        appointmentEntity.setPatient( patientToPatientEntity( appointment.getPatient() ) );
        appointmentEntity.setAppointmentDate( appointment.getAppointmentDate() );
        appointmentEntity.setAppointmentStatus( appointment.getAppointmentStatus() );
        appointmentEntity.setInterval( intervalMapper.toEntity( appointment.getInterval() ) );

        return appointmentEntity;
    }

    protected Role roleEntityToRole(RoleEntity roleEntity) {
        if ( roleEntity == null ) {
            return null;
        }

        Role role = new Role();

        role.setRole( roleEntity.getRole() );

        return role;
    }

    protected List<Role> roleEntityListToRoleList(List<RoleEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<Role> list1 = new ArrayList<Role>( list.size() );
        for ( RoleEntity roleEntity : list ) {
            list1.add( roleEntityToRole( roleEntity ) );
        }

        return list1;
    }

    protected User userEntityToUser(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        User user = new User();

        user.setId( userEntity.getId() );
        user.setEmail( userEntity.getEmail() );
        user.setRoles( roleEntityListToRoleList( userEntity.getRoles() ) );
        user.setKeycloakId( userEntity.getKeycloakId() );

        return user;
    }

    protected Doctor doctorEntityToDoctor(DoctorEntity doctorEntity) {
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
        doctor.setUser( userEntityToUser( doctorEntity.getUser() ) );
        List<SpecialtyEnum> list = doctorEntity.getSpecialties();
        if ( list != null ) {
            doctor.setSpecialties( new ArrayList<SpecialtyEnum>( list ) );
        }
        doctor.setSchedule( scheduleMapper.toDomain( doctorEntity.getSchedule() ) );

        return doctor;
    }

    protected List<Appointment> appointmentEntityListToAppointmentList(List<AppointmentEntity> list) throws Exception {
        if ( list == null ) {
            return null;
        }

        List<Appointment> list1 = new ArrayList<Appointment>( list.size() );
        for ( AppointmentEntity appointmentEntity : list ) {
            list1.add( toDomain( appointmentEntity ) );
        }

        return list1;
    }

    protected Doctor doctorEntityToDoctor1(DoctorEntity doctorEntity) throws Exception {
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
        doctor.setUser( userEntityToUser( doctorEntity.getUser() ) );
        List<SpecialtyEnum> list = doctorEntity.getSpecialties();
        if ( list != null ) {
            doctor.setSpecialties( new ArrayList<SpecialtyEnum>( list ) );
        }
        doctor.setScheduledAppointments( appointmentEntityListToAppointmentList( doctorEntity.getScheduledAppointments() ) );
        doctor.setAttendedAppointments( appointmentEntityListToAppointmentList( doctorEntity.getAttendedAppointments() ) );
        doctor.setSchedule( scheduleMapper.toDomain( doctorEntity.getSchedule() ) );

        return doctor;
    }

    protected List<Doctor> doctorEntityListToDoctorList(List<DoctorEntity> list) throws Exception {
        if ( list == null ) {
            return null;
        }

        List<Doctor> list1 = new ArrayList<Doctor>( list.size() );
        for ( DoctorEntity doctorEntity : list ) {
            list1.add( doctorEntityToDoctor1( doctorEntity ) );
        }

        return list1;
    }

    protected MedicalHistory medicalHistoryEntityToMedicalHistory(MedicalHistoryEntity medicalHistoryEntity) throws Exception {
        if ( medicalHistoryEntity == null ) {
            return null;
        }

        MedicalHistory medicalHistory = new MedicalHistory();

        medicalHistory.setId( medicalHistoryEntity.getId() );
        Map<Date, String> map = medicalHistoryEntity.getMedicalHistory();
        if ( map != null ) {
            medicalHistory.setMedicalHistory( new LinkedHashMap<Date, String>( map ) );
        }
        medicalHistory.setDoctors( doctorEntityListToDoctorList( medicalHistoryEntity.getDoctors() ) );

        return medicalHistory;
    }

    protected Patient patientEntityToPatient(PatientEntity patientEntity) throws Exception {
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
        patient.setUser( userEntityToUser( patientEntity.getUser() ) );
        patient.setMedicalHistory( medicalHistoryEntityToMedicalHistory( patientEntity.getMedicalHistory() ) );
        patient.setAppointmentCount( patientEntity.getAppointmentCount() );

        return patient;
    }

    protected RoleEntity roleToRoleEntity(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleEntity roleEntity = new RoleEntity();

        roleEntity.setRole( role.getRole() );

        return roleEntity;
    }

    protected List<RoleEntity> roleListToRoleEntityList(List<Role> list) {
        if ( list == null ) {
            return null;
        }

        List<RoleEntity> list1 = new ArrayList<RoleEntity>( list.size() );
        for ( Role role : list ) {
            list1.add( roleToRoleEntity( role ) );
        }

        return list1;
    }

    protected UserEntity userToUserEntity(User user) {
        if ( user == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( user.getId() );
        userEntity.setEmail( user.getEmail() );
        userEntity.setKeycloakId( user.getKeycloakId() );
        userEntity.setRoles( roleListToRoleEntityList( user.getRoles() ) );

        return userEntity;
    }

    protected DoctorEntity doctorToDoctorEntity(Doctor doctor) {
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
        doctorEntity.setUser( userToUserEntity( doctor.getUser() ) );
        List<SpecialtyEnum> list = doctor.getSpecialties();
        if ( list != null ) {
            doctorEntity.setSpecialties( new ArrayList<SpecialtyEnum>( list ) );
        }
        doctorEntity.setSchedule( scheduleMapper.toEntity( doctor.getSchedule() ) );

        return doctorEntity;
    }

    protected List<AppointmentEntity> appointmentListToAppointmentEntityList(List<Appointment> list) {
        if ( list == null ) {
            return null;
        }

        List<AppointmentEntity> list1 = new ArrayList<AppointmentEntity>( list.size() );
        for ( Appointment appointment : list ) {
            list1.add( toEntity( appointment ) );
        }

        return list1;
    }

    protected DoctorEntity doctorToDoctorEntity1(Doctor doctor) {
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
        doctorEntity.setUser( userToUserEntity( doctor.getUser() ) );
        List<SpecialtyEnum> list = doctor.getSpecialties();
        if ( list != null ) {
            doctorEntity.setSpecialties( new ArrayList<SpecialtyEnum>( list ) );
        }
        doctorEntity.setScheduledAppointments( appointmentListToAppointmentEntityList( doctor.getScheduledAppointments() ) );
        doctorEntity.setAttendedAppointments( appointmentListToAppointmentEntityList( doctor.getAttendedAppointments() ) );
        doctorEntity.setSchedule( scheduleMapper.toEntity( doctor.getSchedule() ) );

        return doctorEntity;
    }

    protected List<DoctorEntity> doctorListToDoctorEntityList(List<Doctor> list) {
        if ( list == null ) {
            return null;
        }

        List<DoctorEntity> list1 = new ArrayList<DoctorEntity>( list.size() );
        for ( Doctor doctor : list ) {
            list1.add( doctorToDoctorEntity1( doctor ) );
        }

        return list1;
    }

    protected MedicalHistoryEntity medicalHistoryToMedicalHistoryEntity(MedicalHistory medicalHistory) {
        if ( medicalHistory == null ) {
            return null;
        }

        MedicalHistoryEntity medicalHistoryEntity = new MedicalHistoryEntity();

        medicalHistoryEntity.setId( medicalHistory.getId() );
        Map<Date, String> map = medicalHistory.getMedicalHistory();
        if ( map != null ) {
            medicalHistoryEntity.setMedicalHistory( new LinkedHashMap<Date, String>( map ) );
        }
        medicalHistoryEntity.setDoctors( doctorListToDoctorEntityList( medicalHistory.getDoctors() ) );

        return medicalHistoryEntity;
    }

    protected PatientEntity patientToPatientEntity(Patient patient) {
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
        patientEntity.setUser( userToUserEntity( patient.getUser() ) );
        patientEntity.setMedicalHistory( medicalHistoryToMedicalHistoryEntity( patient.getMedicalHistory() ) );
        patientEntity.setAppointmentCount( patient.getAppointmentCount() );

        return patientEntity;
    }
}
