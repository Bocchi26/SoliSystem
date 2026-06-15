package games.solisystem.application.command;
import games.solisystem.application.dto.CrearSolicitudCommand;
import games.solisystem.domain.entity.Notificacion;
import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.entity.TipoSolicitud;
import games.solisystem.domain.entity.Usuario;
import games.solisystem.domain.enums.EstadoEnum;
import games.solisystem.domain.enums.RolEnum;
import games.solisystem.domain.observer.SolicitudObserver;
import games.solisystem.domain.repository.NotificacionRepository;
import games.solisystem.domain.repository.SolicitudRepository;
import games.solisystem.domain.repository.TipoSolicitudRepository;
import games.solisystem.domain.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CrearSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoSolicitudRepository tipoSolicitudRepository;
    private final NotificacionRepository notificacionRepository;
    private final List<SolicitudObserver> observers;

    public CrearSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository,
            TipoSolicitudRepository tipoSolicitudRepository,
            NotificacionRepository notificacionRepository,
            List<SolicitudObserver> observers
    ) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoSolicitudRepository = tipoSolicitudRepository;
        this.notificacionRepository = notificacionRepository;
        this.observers = (observers != null) ? observers : new ArrayList<>();
    }

    public Solicitud ejecutar(CrearSolicitudCommand command) {
        validar(command);

        Usuario usuario = usuarioRepository.buscarPorId(command.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));

        if (usuario.getRol() != RolEnum.SOLICITANTE) {
            throw new IllegalArgumentException("Solo un SOLICITANTE puede crear solicitudes.");
        }

        TipoSolicitud tipoSolicitud = tipoSolicitudRepository.buscarPorId(command.getTipoSolicitudId())
                .orElseThrow(() -> new IllegalArgumentException("El tipo de solicitud no existe."));

        Solicitud solicitud = new Solicitud(
                null,
                usuario,
                tipoSolicitud,
                command.getDescripcion().trim(),
                LocalDate.now(),
                EstadoEnum.CREADA
        );

        solicitudRepository.guardar(solicitud);

        // Si tu NotificacionObserver ya persiste, puedes dejar esto solo como registro del caso de uso.
        if (notificacionRepository != null) {
            Notificacion notificacion = new Notificacion(
                    null,
                    solicitud,
                    "Se creó una nueva solicitud con estado CREADA.",
                    LocalDate.now(),
                    EstadoEnum.CREADA
            );
            notificacionRepository.guardar(notificacion);
        }

        for (SolicitudObserver observer : observers) {
            observer.onSolicitudCreada(solicitud);
        }

        return solicitud;
    }

    private void validar(CrearSolicitudCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo.");
        }
        if (command.getUsuarioId() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio.");
        }
        if (command.getTipoSolicitudId() == null) {
            throw new IllegalArgumentException("El tipo de solicitud es obligatorio.");
        }
        if (command.getDescripcion() == null || command.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }
    }
}