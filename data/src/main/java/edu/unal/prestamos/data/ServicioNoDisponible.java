package edu.unal.prestamos.data;

public class ServicioNoDisponible extends RuntimeException {

    public ServicioNoDisponible(String message) {
        super(message);
    }

    public ServicioNoDisponible(String message, Throwable cause) {
        super(message, cause);
    }
}
