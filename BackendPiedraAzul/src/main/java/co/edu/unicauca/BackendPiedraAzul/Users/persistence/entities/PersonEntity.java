package co.edu.unicauca.BackendPiedraAzul.Users.persistence.entities;

import co.edu.unicauca.BackendPiedraAzul.Users.domain.DocumentTypeEnum;
import co.edu.unicauca.BackendPiedraAzul.Users.domain.GenderEnum;
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
    protected DocumentTypeEnum documentType;
    protected String identificationNumber;
    protected String firstName;
    protected String lastName;
    protected Date birthDate;
    protected String phone;
    protected boolean active;
    @OneToOne(cascade = CascadeType.ALL)
    protected UserEntity user;
    @Enumerated(EnumType.STRING)
    protected GenderEnum gender;
}
