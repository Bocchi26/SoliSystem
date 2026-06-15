package games.solisystem.application.query;

import games.solisystem.application.dto.GenerarReporteQuery;
import games.solisystem.application.dto.ReporteResponse;
import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.enums.EstadoEnum;
import games.solisystem.domain.repository.SolicitudRepository;

import java.util.List;

public class GenerarReporteUseCase {

    private final SolicitudRepository solicitudRepository;

    public GenerarReporteUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public ReporteResponse ejecutar(GenerarReporteQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("La consulta no puede ser nula.");
        }

        List<Solicitud> solicitudes = solicitudRepository.buscarTodos();

        long total = solicitudes.size();
        long creadas = contarPorEstado(solicitudes, EstadoEnum.CREADA);
        long enRevision = contarPorEstado(solicitudes, EstadoEnum.EN_REVISION);
        long aprobadas = contarPorEstado(solicitudes, EstadoEnum.APROBADA);
        long rechazadas = contarPorEstado(solicitudes, EstadoEnum.RECHAZADA);
        long cerradas = contarPorEstado(solicitudes, EstadoEnum.CERRADA);

        return new ReporteResponse(total, creadas, enRevision, aprobadas, rechazadas, cerradas);
    }

    private long contarPorEstado(List<Solicitud> solicitudes, EstadoEnum estado) {
        return solicitudes.stream()
                .filter(s -> s.getEstado() == estado)
                .count();
    }
}