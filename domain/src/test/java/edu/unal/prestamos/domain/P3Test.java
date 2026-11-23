package edu.unal.prestamos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class P3Test {

    @Test
    @DisplayName("Cuando equipo está disponible, guardar el préstamo y notificar")
    void ejecutar_equipoDisponible_guardaYNotifica() {

        DisponibilidadEquipos disponibilidad = mock(DisponibilidadEquipos.class);
        when(disponibilidad.estaDisponible(12)).thenReturn(true);
        PrestamoRepository repositorio = mock(PrestamoRepository.class);
        when(repositorio.siguienteId()).thenReturn(50);
        Notificador notificador = mock(Notificador.class);

        SolicitarPrestamo casoDeUso = new SolicitarPrestamo(disponibilidad, repositorio, notificador);
        Prestamo p = casoDeUso.ejecutar("1023", 12);

        assertEquals(EstadoPrestamo.PENDIENTE, p.getEstado());
        verify(repositorio, times(1)).guardar(p);
        verify(notificador, times(1)).notificar("1023", "Solicitud " + 50 + " registrada");
    }

    @Test
    @DisplayName("Cuando equipo no está disponible, se lanza EquipoNoDisponible, no se guarda ni notifica")
    void  ejecutar_equipoNoDisponible_noGuardaNiNotifica() {
            
        DisponibilidadEquipos disponibilidad = mock(DisponibilidadEquipos.class);
        when(disponibilidad.estaDisponible(12)).thenReturn(false);
        PrestamoRepository repositorio = mock(PrestamoRepository.class);
        // when(repositorio.siguienteId()).thenReturn(50);
        Notificador notificador = mock(Notificador.class);
        
        SolicitarPrestamo casoDeUso = new SolicitarPrestamo(disponibilidad, repositorio, notificador);
        assertThrows(
            EquipoNoDisponible.class,
            () -> casoDeUso.ejecutar("1023", 12)
        );

        verify(repositorio, never()).guardar(any(Prestamo.class));
        verifyNoInteractions(notificador);
    }

    @Test
    @DisplayName("Al ejecutar guardar() se presenta error 'BD caida', no notificar")
    void  ejecutar_falloAlGuardar_noNotifica() {
            
        DisponibilidadEquipos disponibilidad = mock(DisponibilidadEquipos.class);
        when(disponibilidad.estaDisponible(12)).thenReturn(true);
        PrestamoRepository repositorio = mock(PrestamoRepository.class);
        when(repositorio.siguienteId()).thenReturn(50);
        doThrow(new RuntimeException("BD caída"))
            .when(repositorio)
            .guardar(any(Prestamo.class));
        Notificador notificador = mock(Notificador.class);

        SolicitarPrestamo casoDeUso = new SolicitarPrestamo(disponibilidad, repositorio, notificador);
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> casoDeUso.ejecutar("1023", 12)
        );

        assertEquals("BD caída", exception.getMessage() );
        verify(repositorio, times(1)).guardar(any(Prestamo.class));
        verifyNoInteractions(notificador);
    }
}
