package edu.unal.prestamos.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculadoraMulta {

    private static final int MULTA_POR_DIA = 2_000;

    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        long diasDeRetraso = ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion);
        if (diasDeRetraso <= 0) {
            return 0;
        }
        return (int) diasDeRetraso * MULTA_POR_DIA;
    }
}
