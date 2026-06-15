package games.solisystem.application;

import games.solisystem.domain.entity.Notificacion;
import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.observer.SolicitudObserver;
import games.solisystem.domain.repository.NotificacionRepository;
import java.time.LocalDate;

public class NotificacionObserver implements SolicitudObserver {
    private final NotificacionRepository notificacionRepository;

    public NotificacionObserver(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public void onCambioEstado(Solicitud solicitud) {
        String mensaje = "La solicitud #" + solicitud.getId() + " ha cambiado al estado: " + solicitud.getEstado();
        Notificacion notificacion = new Notificacion(null, solicitud, mensaje, LocalDate.now(), solicitud.getEstado());
        notificacionRepository.guardar(notificacion);
        System.out.println("[Observer Alert] " + mensaje);
    }
}