package edu.unal.prestamos.data;

import edu.unal.prestamos.domain.EstadoPrestamo;
import edu.unal.prestamos.domain.Notificador;
import edu.unal.prestamos.domain.Prestamo;
import edu.unal.prestamos.domain.SolicitarPrestamo;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SolicitarPrestamoIntegracionTest {

    private MockWebServer servidor;

    @BeforeEach
    void setUp() throws IOException {
        servidor = new MockWebServer();
        servidor.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        servidor.shutdown();
    }

    @Test
    @DisplayName("(d) Integración extremo a extremo: SolicitarPrestamo guarda el préstamo en el fake")
    void ejecutar_equipoDisponibleEnServidorHttp_guardaPrestamoEnFakeYNotifica() throws InterruptedException {
        servidor.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\": 12, \"nombre\": \"Portatil\", \"estado\": \"disponible\"}"));

        DisponibilidadEquiposHttp disponibilidad = new DisponibilidadEquiposHttp(servidor.url("").toString());
        PrestamoRepositoryEnMemoria repositorio = new PrestamoRepositoryEnMemoria();
        Notificador notificador = mock(Notificador.class);

        SolicitarPrestamo casoDeUso = new SolicitarPrestamo(disponibilidad, repositorio, notificador);
        Prestamo resultado = casoDeUso.ejecutar("1023", 12);

        assertEquals(1, repositorio.obtenerTodos().size());
        Prestamo guardado = repositorio.obtenerTodos().get(0);
        assertSame(resultado, guardado);
        assertEquals(1, guardado.getId());
        assertEquals("1023", guardado.getEstudianteId());
        assertEquals(12, guardado.getEquipoId());
        assertEquals(EstadoPrestamo.PENDIENTE, guardado.getEstado());

        RecordedRequest peticion = servidor.takeRequest();
        assertEquals("GET", peticion.getMethod());
        assertEquals("/api/v1/equipos/12", peticion.getPath());

        verify(notificador, times(1)).notificar("1023", "Solicitud 1 registrada");
    }
}
