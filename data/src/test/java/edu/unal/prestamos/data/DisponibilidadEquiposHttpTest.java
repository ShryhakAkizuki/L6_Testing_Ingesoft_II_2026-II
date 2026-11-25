package edu.unal.prestamos.data;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DisponibilidadEquiposHttpTest {

    private MockWebServer servidor;
    private DisponibilidadEquiposHttp adaptador;

    @BeforeEach
    void setUp() throws IOException {
        servidor = new MockWebServer();
        servidor.start();
        adaptador = new DisponibilidadEquiposHttp(servidor.url("").toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        servidor.shutdown();
    }

    @Test
    @DisplayName("200 con estado disponible devuelve verdadero y llama GET /api/v1/equipos/12")
    void estaDisponible_equipoDisponible_devuelveVerdaderoYLlamaLaRutaCorrecta() throws InterruptedException {
        servidor.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\": 12, \"nombre\": \"Portatil\", \"estado\": \"disponible\"}"));

        assertTrue(adaptador.estaDisponible(12));

        RecordedRequest peticion = servidor.takeRequest();
        assertEquals("GET", peticion.getMethod());
        assertEquals("/api/v1/equipos/12", peticion.getPath());
    }

    @Test
    @DisplayName("(a) 200 con estado prestado devuelve falso")
    void estaDisponible_equipoPrestado_devuelveFalso() {
        servidor.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\": 12, \"nombre\": \"Portatil\", \"estado\": \"prestado\"}"));

        assertFalse(adaptador.estaDisponible(12));
    }

    @Test
    @DisplayName("(b) 404 devuelve falso")
    void estaDisponible_respuesta404_devuelveFalso() {
        servidor.enqueue(new MockResponse().setResponseCode(404));

        assertFalse(adaptador.estaDisponible(12));
    }

    @Test
    @DisplayName("(c) 500 lanza ServicioNoDisponible con mensaje HTTP 500")
    void estaDisponible_respuesta500_lanzaServicioNoDisponible() {
        servidor.enqueue(new MockResponse().setResponseCode(500));

        ServicioNoDisponible excepcion = assertThrows(
                ServicioNoDisponible.class,
                () -> adaptador.estaDisponible(12)
        );

        assertEquals("HTTP 500", excepcion.getMessage());
    }

    @Test
    @DisplayName("Un error de red lanza ServicioNoDisponible y conserva la causa")
    void estaDisponible_errorDeRed_lanzaServicioNoDisponible() throws IOException {
        String urlBase = servidor.url("").toString();
        servidor.shutdown();
        adaptador = new DisponibilidadEquiposHttp(urlBase);

        ServicioNoDisponible excepcion = assertThrows(
                ServicioNoDisponible.class,
                () -> adaptador.estaDisponible(12)
        );

        assertNotNull(excepcion.getCause());
    }
}
