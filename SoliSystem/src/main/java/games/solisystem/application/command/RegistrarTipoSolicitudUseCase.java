package games.solisystem.application.command;

import games.solisystem.application.dto.RegistrarTipoSolicitudCommand;
import games.solisystem.domain.entity.TipoSolicitud;
import games.solisystem.domain.repository.TipoSolicitudRepository;

public class RegistrarTipoSolicitudUseCase {

    private final TipoSolicitudRepository tipoSolicitudRepository;

    public RegistrarTipoSolicitudUseCase(TipoSolicitudRepository tipoSolicitudRepository) {
        this.tipoSolicitudRepository = tipoSolicitudRepository;
    }

    public TipoSolicitud ejecutar(RegistrarTipoSolicitudCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo.");
        }

        // Validación de duplicados en la persistencia (Regla de aplicación)
        if (tipoSolicitudRepository.buscarPorNombre(command.getNombre()).isPresent()) {
            throw new IllegalArgumentException("El tipo de solicitud ya existe con ese nombre.");
        }

        // El constructor de TipoSolicitud valida que el nombre/descripción no estén vacíos
        // y que el tiempo estimado en días sea estrictamente mayor a 0.
        TipoSolicitud tipoSolicitud = new TipoSolicitud(
                command.getNombre(),
                command.getDescripcion(),
                command.getTiempoEstimadoDias()
        );

        // CORRECCIÓN: Guardar (operación void) y luego retornar el objeto creado
        this.tipoSolicitudRepository.guardar(tipoSolicitud);

        return tipoSolicitud;
    }
}