package Appointments.persistence.entities;

import Appointments.domain.RoleEnum;
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
public class RoleEntity {
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}
