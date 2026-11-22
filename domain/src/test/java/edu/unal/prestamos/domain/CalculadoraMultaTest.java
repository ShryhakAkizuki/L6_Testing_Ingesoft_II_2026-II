package edu.unal.prestamos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraMultaTest {

    private static final LocalDate LIMITE = LocalDate.of(2026, 11, 25);

    @Test
    @DisplayName("calcular_devueltoAntesDelLimite_noHayMulta")
    void calcular_devueltoAntesDelLimite_noHayMulta() {

        CalculadoraMulta calculadora = new CalculadoraMulta();
        int multa = calculadora.calcular(LIMITE, LIMITE.minusDays(1), false);

        assertEquals(0, multa);
    }

    @Test
    @DisplayName("calcular_unDiaDeRetraso_multaDosMilPesos")
    void calcular_unDiaDeRetraso_multaDosMilPesos() {

        CalculadoraMulta calculadora = new CalculadoraMulta();
        int multa = calculadora.calcular(LIMITE, LIMITE.plusDays(1), false);

        assertEquals(2000, multa);
    }

    @Test
    @DisplayName("calcular_devueltoExactamenteElDiaLimite_noHayMulta")
    void calcular_devueltoExactamenteElDiaLimite_noHayMulta() {

        CalculadoraMulta calculadora = new CalculadoraMulta();
        int multa = calculadora.calcular(LIMITE, LIMITE, false);

        assertEquals(0, multa);
    }
}
