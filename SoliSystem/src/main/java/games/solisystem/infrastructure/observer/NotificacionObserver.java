package games.solisystem.infrastructure.observer;

import java.time.LocalDate;

import games.solisystem.domain.entity.Notificacion;
import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.observer.SolicitudObserver;
import games.solisystem.domain.repository.NotificacionRepository;

public class NotificacionObserver implements SolicitudObserver {

    private final NotificacionRepository notificacionRepository;

    public NotificacionObserver(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public void onSolicitudCreada(Solicitud solicitud) {
        String mensaje = "Se creó una nueva solicitud: '" + solicitud.getDescripcion() + "' con estado " + solicitud.getEstado().name();
        Notificacion notificacion = new Notificacion(
            solicitud,
            mensaje,
            LocalDate.now(),
            solicitud.getEstado().name()
        );
        notificacionRepository.guardar(notificacion);
        System.out.println("[NOTIFICACIÓN] " + mensaje);
    }

    @Override
    public void onEstadoCambiado(Solicitud solicitud) {
        String mensaje = "La solicitud #" + solicitud.getId() + " cambió al estado: " + solicitud.getEstado().name();
        Notificacion notificacion = new Notificacion(
            solicitud,
            mensaje,
            LocalDate.now(),
            solicitud.getEstado().name()
        );
        notificacionRepository.guardar(notificacion);
        System.out.println("[NOTIFICACIÓN] " + mensaje);
    }
}