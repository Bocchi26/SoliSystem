package games.solisystem.application.query;

import games.solisystem.application.dto.ConsultarPorEstadoQuery;
import games.solisystem.application.dto.SolicitudResponse;
import games.solisystem.domain.enums.EstadoSolicitud;
import games.solisystem.domain.repository.SolicitudRepository;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultarSolicitudesPorEstadoUseCase {
    private final SolicitudRepository solicitudRepo;

    public ConsultarSolicitudesPorEstadoUseCase(SolicitudRepository solicitudRepo) {
        this.solicitudRepo = solicitudRepo;
    }

    public List<SolicitudResponse> ejecutar(ConsultarPorEstadoQuery query) {
        EstadoSolicitud estado = EstadoSolicitud.valueOf(query.estado.toUpperCase());
        return solicitudRepo.buscarPorEstado(estado).stream()
                .map(SolicitudResponse::new)
                .collect(Collectors.toList());
    }
}