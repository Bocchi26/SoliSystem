package games.solisystem.application.query;

import java.util.List;

import games.solisystem.domain.entity.TipoSolicitud;
import games.solisystem.domain.repository.TipoSolicitudRepository;

public class ListarTiposSolicitudUseCase {
    private final TipoSolicitudRepository tipoSolicitudRepository;

    public ListarTiposSolicitudUseCase(TipoSolicitudRepository tipoSolicitudRepository) {
        this.tipoSolicitudRepository = tipoSolicitudRepository;
    }

    public List<TipoSolicitud> ejecutar() {
        return tipoSolicitudRepository.buscarTodos();
    }
}