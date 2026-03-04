package Agendas.Entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class IntervaloList {
    @ElementCollection
    private List<Intervalo> intervalos;
}
