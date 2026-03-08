package Appointments.persistence.entities;

import Appointments.domain.DoctorTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class PersonEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;
    @Enumerated(EnumType.STRING)
    protected DoctorTypeEnum documentType;
    protected String identificationNumber;
    protected String firstName;
    protected String lastName;
    protected Date birthDate;
    protected String phone;
    protected boolean active;
    @OneToOne
    protected UserEntity user;
}
