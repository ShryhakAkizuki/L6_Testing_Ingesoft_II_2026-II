package edu.unal.prestamos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrestamoTest {

    private static final LocalDate HOY = LocalDate.of(2026, 11, 18);

    private Prestamo prestamo;

    @BeforeEach
    void crearPrestamoPendiente() {
        prestamo = new Prestamo(1, "1023", 12);
    }

    @Test
    @DisplayName("1. aprobar un pendiente → APROBADO con límite en 7 días")
    void aprobar_prestamoPendiente_quedaAprobadoConLimiteEnSieteDias() {

        prestamo.aprobar(HOY);

        assertEquals(EstadoPrestamo.APROBADO, prestamo.getEstado());
        assertEquals(HOY.plusDays(7), prestamo.getFechaLimite());
    }

    @Test
    @DisplayName("2. renovar por segunda vez → lanza error")
    void renovar_porSegundaVez_lanzaError() {

        prestamo.aprobar(HOY);
        prestamo.renovar();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> prestamo.renovar());
        assertEquals("El préstamo ya fue renovado", ex.getMessage());
    }

    @Test
    @DisplayName("3. renovar una vez extiende el límite 7 días desde HOY")
    void renovar_unaVez_extiendeALosCatorceDiasDesdeHoy() {

        prestamo.aprobar(HOY);
        prestamo.renovar();

        assertEquals(HOY.plusDays(14), prestamo.getFechaLimite());
        assertEquals(1, prestamo.getRenovaciones());
    }

    @Test
    @DisplayName("4. aprobar un préstamo ya aprobado → lanza error")
    void aprobar_prestamoYaAprobado_lanzaError() {

        prestamo.aprobar(HOY);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> prestamo.aprobar(HOY));
        assertEquals("Solo se aprueba un préstamo pendiente", ex.getMessage());
    }

    @Test
    @DisplayName("5. renovar un pendiente → lanza error")
    void renovar_prestamoPendiente_lanzaError() {

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> prestamo.renovar());
        assertEquals("Solo se renueva un préstamo aprobado", ex.getMessage());
    }

    @Test
    @DisplayName("6. devolver un aprobado → DEVUELTO")
    void devolver_prestamoAprobado_quedaDevuelto() {

        prestamo.aprobar(HOY);
        prestamo.devolver();

        assertEquals(EstadoPrestamo.DEVUELTO, prestamo.getEstado());
    }

    @Test
    @DisplayName("7. devolver uno ya devuelto → lanza error")
    void devolver_prestamoYaDevuelto_lanzaError() {

        prestamo.aprobar(HOY);
        prestamo.devolver();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> prestamo.devolver());
        assertEquals("Solo se devuelve un préstamo aprobado", ex.getMessage());
    }
}
