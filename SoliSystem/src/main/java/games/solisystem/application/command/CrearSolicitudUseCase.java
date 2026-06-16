package games.solisystem.application.command;

import java.util.ArrayList;
import java.util.List;

import games.solisystem.application.dto.CrearSolicitudCommand;
import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.entity.TipoSolicitud;
import games.solisystem.domain.entity.Usuario;
import games.solisystem.domain.observer.SolicitudObserver;
import games.solisystem.domain.repository.SolicitudRepository;
import games.solisystem.domain.repository.TipoSolicitudRepository;
import games.solisystem.domain.repository.UsuarioRepository;

public class CrearSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoSolicitudRepository tipoSolicitudRepository;
    private final List<SolicitudObserver> observers;

    public CrearSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository,
            TipoSolicitudRepository tipoSolicitudRepository,
            List<SolicitudObserver> observers) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoSolicitudRepository = tipoSolicitudRepository;
        this.observers = (observers != null) ? observers : new ArrayList<>();
    }

    public Solicitud ejecutar(CrearSolicitudCommand command) {
        validarComando(command);

        Usuario usuario = usuarioRepository.buscarPorId(command.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));

        TipoSolicitud tipoSolicitud = tipoSolicitudRepository.buscarPorId(command.getTipoSolicitudId())
                .orElseThrow(() -> new IllegalArgumentException("El tipo de solicitud no existe."));

        // El constructor semántico valida internamente:
        // 1. Que el usuario tenga el rol SOLICITANTE.
        // 2. Que la descripción no esté vacía.
        // Además, asigna automáticamente la fecha actual y el EstadoEnum.CREADA.
        Solicitud solicitud = new Solicitud(usuario, tipoSolicitud, command.getDescripcion());

        solicitudRepository.guardar(solicitud);

        for (SolicitudObserver observer : observers) {
            observer.onSolicitudCreada(solicitud);
        }

        return solicitud;
    }

    private void validarComando(CrearSolicitudCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo.");
        }
        if (command.getUsuarioId() == null) {
            throw new IllegalArgumentException("El id del usuario es obligatorio.");
        }
        if (command.getTipoSolicitudId() == null) {
            throw new IllegalArgumentException("El id del tipo de solicitud es obligatorio.");
        }
    }
}