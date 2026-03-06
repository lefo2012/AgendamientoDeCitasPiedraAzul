package domainTest;


import Citas.entities.Intervalo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

public class IntervaloTest {

    Intervalo intervalo;

    @Test
    void isInOfTrueTest()
    {
        intervalo = new Intervalo(LocalTime.of(11,00),LocalTime.of(21,00));

        Intervalo intervalo1 = new Intervalo(LocalTime.of(11,00),LocalTime.of(21,00));

        Assertions.assertTrue(intervalo.isInOf(intervalo1));
    }
    @Test
    void isInOfFalseTest()
    {
        intervalo = new Intervalo(LocalTime.of(11,00),LocalTime.of(22,00));

        Intervalo intervalo1 = new Intervalo(LocalTime.of(11,00),LocalTime.of(21,00));

        Assertions.assertFalse(intervalo.isInOf(intervalo1));
    }
    @Test
    void overloadsTrueTest()
    {
        intervalo = new Intervalo(LocalTime.of(20,59),LocalTime.of(22,00));

        Intervalo intervalo1 = new Intervalo(LocalTime.of(11,00),LocalTime.of(21,00));

        Assertions.assertTrue(intervalo.overlaps(intervalo1));
    }
    @Test
    void overloadsFalseTest()
    {
        intervalo = new Intervalo(LocalTime.of(21,59),LocalTime.of(22,00));

        Intervalo intervalo1 = new Intervalo(LocalTime.of(11,00),LocalTime.of(21,00));

        Assertions.assertFalse(intervalo.overlaps(intervalo1));
    }
    @Test
    void equalsTrueTest()
    {
        intervalo = new Intervalo(LocalTime.of(21,59),LocalTime.of(22,00));
        Intervalo intervalo1 = new Intervalo(LocalTime.of(21,59),LocalTime.of(22,00));
        assertEquals(intervalo, intervalo1);
    }
    @Test
    void equalsFalseTest()
    {

        intervalo = new Intervalo(LocalTime.of(21,58),LocalTime.of(22,00));
        Intervalo intervalo1 = new Intervalo(LocalTime.of(21,59),LocalTime.of(22,00));
        assertNotEquals(intervalo, intervalo1);
    }


}
