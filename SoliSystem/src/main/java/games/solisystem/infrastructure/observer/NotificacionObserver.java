package games.solisystem.infrastructure.observer;

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
        
        // CORRECCIÓN: Usamos el nuevo constructor semántico rico del dominio
        Notificacion notificacion = new Notificacion(
            solicitud,
            mensaje,
            solicitud.getEstado()
        );
        
        notificacionRepository.guardar(notificacion);
        System.out.println("[NOTIFICACIÓN] " + mensaje);
    }

    @Override
    public void onEstadoCambiado(Solicitud solicitud) {
        String mensaje = "La solicitud #" + solicitud.getId() + " cambió al estado: " + solicitud.getEstado().name();
        
        // CORRECCIÓN: Usamos el nuevo constructor semántico rico del dominio
        Notificacion notificacion = new Notificacion(
            solicitud,
            mensaje,
            solicitud.getEstado()
        );
        
        notificacionRepository.guardar(notificacion);
        System.out.println("[NOTIFICACIÓN] " + mensaje);
    }
}