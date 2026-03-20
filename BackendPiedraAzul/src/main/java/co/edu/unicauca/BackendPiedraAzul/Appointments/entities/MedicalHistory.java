package co.edu.unicauca.BackendPiedraAzul.Appointments.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    //A map will be used for easier search by date
    //I don't know how to save maps in JPA
    @ElementCollection
    private Map<Date, String> medicalHistory;
    //We need to look at this carefully if it's interesting or if we can leave it just in the history as such
    @OneToMany
    private List<Doctor> doctors;

}
