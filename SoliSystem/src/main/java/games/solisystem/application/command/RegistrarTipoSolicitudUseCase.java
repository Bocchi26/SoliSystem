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
        validar(command);

        TipoSolicitud tipoSolicitud = new TipoSolicitud(
                null,
                command.getNombre().trim(),
                command.getDescripcion().trim(),
                command.getTiempoEstimadoDias());

        tipoSolicitudRepository.guardar(tipoSolicitud);
        return tipoSolicitud;
    }

    private void validar(RegistrarTipoSolicitudCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo.");
        }
        if (command.getNombre() == null || command.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (command.getDescripcion() == null || command.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion es obligatoria.");
        }
        if (command.getTiempoEstimadoDias() <= 0) {
            throw new IllegalArgumentException("El tiempo estimado debe ser mayor que cero.");
        }
    }
}