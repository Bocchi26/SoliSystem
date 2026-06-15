package games.solisystem.application.query;

import games.solisystem.application.dto.ConsultarPorEstadoQuery;
import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.repository.SolicitudRepository;

import java.util.List;

public class ConsultarSolicitudesPorEstadoUseCase {

    private final SolicitudRepository solicitudRepository;

    public ConsultarSolicitudesPorEstadoUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public List<Solicitud> ejecutar(ConsultarPorEstadoQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("La consulta no puede ser nula.");
        }
        if (query.getEstado() == null) {
            throw new IllegalArgumentException("El estado es obligatorio.");
        }
        return solicitudRepository.buscarPorEstado(query.getEstado());
    }
}