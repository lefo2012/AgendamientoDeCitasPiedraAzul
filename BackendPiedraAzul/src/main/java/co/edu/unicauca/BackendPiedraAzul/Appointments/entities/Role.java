package co.edu.unicauca.BackendPiedraAzul.Appointments.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class Role {
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}
