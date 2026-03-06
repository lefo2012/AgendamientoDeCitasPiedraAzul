package Citas.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
public class IntervaloList {
    @ElementCollection
    private List<Intervalo> intervalos;

    public IntervaloList()
    {
        intervalos=new ArrayList<>();
    }

}
