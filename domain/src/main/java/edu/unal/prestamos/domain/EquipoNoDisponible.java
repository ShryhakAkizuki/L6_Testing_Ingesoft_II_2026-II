package edu.unal.prestamos.domain;

public class EquipoNoDisponible extends RuntimeException {
    public EquipoNoDisponible(int equipoId) {
        super("Equipo " + equipoId + " no disponible");
    }

}
