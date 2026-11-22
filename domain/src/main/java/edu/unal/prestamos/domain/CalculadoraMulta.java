package edu.unal.prestamos.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculadoraMulta {

    private static final int MULTA_POR_DIA = 2_000;
    private static final int MULTA_MAXIMA = 50_000;
    private static final int DIVISOR_MONITOR = 2;

    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        int diasDeRetraso = diasDeRetraso(fechaLimite, fechaDevolucion);
        if (diasDeRetraso <= 0) {
            return 0;
        }
        int multa = Math.min(diasDeRetraso * MULTA_POR_DIA, MULTA_MAXIMA);
        return esMonitor ? multa / DIVISOR_MONITOR : multa;
    }

    private static int diasDeRetraso(LocalDate fechaLimite, LocalDate fechaDevolucion) {
        return (int) ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion);
    }
}
