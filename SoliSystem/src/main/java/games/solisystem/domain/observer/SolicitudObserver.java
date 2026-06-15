package games.solisystem.domain.observer;

import games.solisystem.domain.entity.Solicitud;

public interface SolicitudObserver {
    void onSolicitudCreada(Solicitud solicitud);
    void onCambioEstado(Solicitud solicitud);
}