package games.solisystem.application.command;

import java.util.ArrayList;
import java.util.List;

import games.solisystem.application.dto.CambiarEstadoCommand;
import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.observer.SolicitudObserver;
import games.solisystem.domain.repository.SolicitudRepository;

public class CambiarEstadoSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final List<SolicitudObserver> observers;

    public CambiarEstadoSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            List<SolicitudObserver> observers) {
        this.solicitudRepository = solicitudRepository;
        this.observers = (observers != null) ? observers : new ArrayList<>();
    }

    public Solicitud ejecutar(CambiarEstadoCommand command) {
        validarComando(command);
        
        Solicitud solicitud = solicitudRepository.buscarPorId(command.getSolicitudId())
                .orElseThrow(() -> new IllegalArgumentException("La solicitud no existe."));
        
        // Delegamos la lógica empresarial de transición enteramente a la entidad de dominio
        solicitud.cambiarEstado(command.getNuevoEstado());
        
        solicitudRepository.actualizar(solicitud);

        // Notificar a los observadores de la infraestructura/aplicación
        for (SolicitudObserver observer : observers) {
            observer.onEstadoCambiado(solicitud);
        }

        return solicitud;
    }

    // Esta validación sí pertenece a la aplicación (valida los datos de entrada del DTO/Comando)
    private void validarComando(CambiarEstadoCommand command) {
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
}