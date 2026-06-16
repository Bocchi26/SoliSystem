package games.solisystem.application.query;

import java.util.List;

import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.repository.SolicitudRepository;

public class ListarTodasLasSolicitudesUseCase {
    private final SolicitudRepository solicitudRepository;

    public ListarTodasLasSolicitudesUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public List<Solicitud> ejecutar() {
        return solicitudRepository.buscarTodos();
    }
}