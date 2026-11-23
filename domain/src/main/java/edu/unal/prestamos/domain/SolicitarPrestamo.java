package edu.unal.prestamos.domain;

public class SolicitarPrestamo {
    
    final DisponibilidadEquipos disponibilidad;
    final PrestamoRepository repositorio;
    final Notificador notificador;

    SolicitarPrestamo(DisponibilidadEquipos disponibilidad, PrestamoRepository repositorio, Notificador notificador) {
        this.disponibilidad = disponibilidad;
        this.repositorio = repositorio;
        this.notificador = notificador;
    }
    
    public Prestamo ejecutar(String estudianteId, int equipoId) {
        if (!disponibilidad.estaDisponible(equipoId)) {
            throw new EquipoNoDisponible(equipoId);
        }   
        Prestamo p = new Prestamo(repositorio.siguienteId(), estudianteId, equipoId);
        repositorio.guardar(p);
        notificador.notificar(estudianteId, "Solicitud " + p.getId() + " registrada");
        return p;
    }
}
