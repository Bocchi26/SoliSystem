package games.solisystem.domain.observer;

import games.solisystem.domain.entity.Solicitud;

/**
 * Interfaz del patrón Observer para el dominio de solicitudes.
 * Define los eventos que los observadores pueden escuchar.
 */
public interface SolicitudObserver {
    void onSolicitudCreada(Solicitud solicitud);
    void onEstadoCambiado(Solicitud solicitud);
}