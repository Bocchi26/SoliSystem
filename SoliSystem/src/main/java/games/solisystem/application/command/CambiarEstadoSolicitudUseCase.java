package games.solisystem.application.command;

import games.solisystem.application.dto.CambiarEstadoCommand;
import games.solisystem.domain.entity.Notificacion;
import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.enums.EstadoEnum;
import games.solisystem.domain.observer.SolicitudObserver;
import games.solisystem.domain.repository.NotificacionRepository;
import games.solisystem.domain.repository.SolicitudRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CambiarEstadoSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final NotificacionRepository notificacionRepository;
    private final List<SolicitudObserver> observers;

    public CambiarEstadoSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            NotificacionRepository notificacionRepository,
            List<SolicitudObserver> observers) {
        this.solicitudRepository = solicitudRepository;
        this.notificacionRepository = notificacionRepository;
        this.observers = (observers != null) ? observers : new ArrayList<>();
    }

    public Solicitud ejecutar(CambiarEstadoCommand command) {
        validar(command);

        Solicitud solicitud = solicitudRepository.buscarPorId(command.getSolicitudId())
                .orElseThrow(() -> new IllegalArgumentException("La solicitud no existe."));

        validarTransicion(solicitud.getEstado(), command.getNuevoEstado());

        solicitud.cambiarEstado(command.getNuevoEstado());
        solicitudRepository.actualizar(solicitud);

        if (notificacionRepository != null) {
            Notificacion notificacion = new Notificacion(
                    null,
                    solicitud,
                    "La solicitud cambió de estado a " + command.getNuevoEstado(),
                    LocalDate.now(),
                    command.getNuevoEstado());
            notificacionRepository.guardar(notificacion);
        }

        for (SolicitudObserver observer : observers) {
            observer.onEstadoCambiado(solicitud);
        }

        return solicitud;
    }

    private void validar(CambiarEstadoCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo.");
        }
        if (command.getSolicitudId() == null) {
            throw new IllegalArgumentException("El id de la solicitud es obligatorio.");
        }
        if (command.getNuevoEstado() == null) {
            throw new IllegalArgumentException("El nuevo estado es obligatorio.");
        }
    }

    private void validarTransicion(EstadoEnum actual, EstadoEnum nuevo) {
        boolean permitida = (actual == EstadoEnum.CREADA && nuevo == EstadoEnum.EN_REVISION) ||
                (actual == EstadoEnum.EN_REVISION && (nuevo == EstadoEnum.APROBADA || nuevo == EstadoEnum.RECHAZADA)) ||
                ((actual == EstadoEnum.APROBADA || actual == EstadoEnum.RECHAZADA) && nuevo == EstadoEnum.CERRADA) ||
                (actual == nuevo);

        if (!permitida) {
            throw new IllegalArgumentException("Transición no permitida de " + actual + " a " + nuevo);
        }
    }
}