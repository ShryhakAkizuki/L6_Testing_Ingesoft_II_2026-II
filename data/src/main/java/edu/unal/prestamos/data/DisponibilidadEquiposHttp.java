package edu.unal.prestamos.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unal.prestamos.domain.DisponibilidadEquipos;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DisponibilidadEquiposHttp implements DisponibilidadEquipos {

    private final String urlBase;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DisponibilidadEquiposHttp(String urlBase) {
        this.urlBase = urlBase.endsWith("/")
                ? urlBase.substring(0, urlBase.length() - 1)
                : urlBase;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean estaDisponible(int equipoId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlBase + "/api/v1/equipos/" + equipoId))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            int statusCode = response.statusCode();
            if (statusCode == 200) {
                JsonNode json = objectMapper.readTree(response.body());
                JsonNode estado = json.get("estado");
                return estado != null && "disponible".equals(estado.asText());
            }
            if (statusCode == 404) {
                return false;
            }
            throw new ServicioNoDisponible("HTTP " + statusCode);
        } catch (IOException e) {
            throw new ServicioNoDisponible(e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServicioNoDisponible(e.getMessage(), e);
        }
    }
}
