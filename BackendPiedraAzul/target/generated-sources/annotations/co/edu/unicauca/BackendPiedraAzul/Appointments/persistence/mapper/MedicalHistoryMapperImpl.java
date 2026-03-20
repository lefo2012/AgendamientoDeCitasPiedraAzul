package co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.mapper;

import co.edu.unicauca.BackendPiedraAzul.Appointments.domain.MedicalHistory;
import co.edu.unicauca.BackendPiedraAzul.Appointments.persistence.entities.MedicalHistoryEntity;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.Doctor;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities.DoctorEntity;
import co.edu.unicauca.BackendPiedraAzul.Users.persistence.mapper.DoctorMapper;
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
public class MedicalHistoryMapperImpl implements MedicalHistoryMapper {

    @Autowired
    private DoctorMapper doctorMapper;

    @Override
    public MedicalHistory toDomain(MedicalHistoryEntity medicalHistoryEntity) {
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

    @Override
    public MedicalHistoryEntity toEntity(MedicalHistory medicalHistory) {
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

    protected List<Doctor> doctorEntityListToDoctorList(List<DoctorEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<Doctor> list1 = new ArrayList<Doctor>( list.size() );
        for ( DoctorEntity doctorEntity : list ) {
            list1.add( doctorMapper.toDomain( doctorEntity ) );
        }

        return list1;
    }

    protected List<DoctorEntity> doctorListToDoctorEntityList(List<Doctor> list) {
        if ( list == null ) {
            return null;
        }

        List<DoctorEntity> list1 = new ArrayList<DoctorEntity>( list.size() );
        for ( Doctor doctor : list ) {
            list1.add( doctorMapper.toEntity( doctor ) );
        }

        return list1;
    }
}
