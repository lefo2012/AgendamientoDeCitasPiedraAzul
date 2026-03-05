package Citas.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class Intervalo {

    private LocalTime inicio;
    private LocalTime fin;

    public Intervalo(LocalTime inicio, LocalTime fin) {

        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Inicio o fin null");
        }

        if (!fin.isAfter(inicio)) {
            throw new IllegalArgumentException("Fin debe ser posterior a inicio");
        }

        this.inicio = inicio;
        this.fin = fin;
    }


    //Hay que sobreescribir para que no compare referencias en memoria si no que compare el contenido lo mismo con los Date
    @Override
    public boolean equals(Object o) {
        if (o instanceof Intervalo) {
            Intervalo intervalo = (Intervalo) o;

            if (this.inicio.equals(intervalo.inicio) && this.fin.equals(intervalo.fin)) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inicio, fin);
    }

    public boolean estaDentroDe(Intervalo intervaloARevisar) {
        return !this.inicio.isBefore(intervaloARevisar.inicio) && !this.fin.isAfter(intervaloARevisar.fin);
    }

    public boolean seSolapa(Intervalo intervaloARevisar) {
        return this.inicio.isBefore(intervaloARevisar.fin) && intervaloARevisar.inicio.isBefore(this.fin);
    }
}
