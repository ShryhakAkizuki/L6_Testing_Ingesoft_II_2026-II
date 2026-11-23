package edu.unal.prestamos.domain;

public interface PrestamoRepository {
    public int siguienteId();

    public void guardar(Prestamo prestamo);
}
