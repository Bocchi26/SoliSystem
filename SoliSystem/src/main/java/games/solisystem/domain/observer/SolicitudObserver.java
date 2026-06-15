package games.solisystem.domain.observer;

import games.solisystem.domain.entity.Solicitud;

public interface SolicitudObserver {
    void onCambioEstado(Solicitud solicitud);
}