package games.solisystem.application.query;

import java.util.List;

import games.solisystem.application.dto.GenerarReporteQuery;
import games.solisystem.application.dto.ReporteResponse;
import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.enums.EstadoEnum;
import games.solisystem.domain.repository.SolicitudRepository;

public class GenerarReporteUseCase {

    private final SolicitudRepository solicitudRepository;

    public GenerarReporteUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public ReporteResponse ejecutar(GenerarReporteQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("La consulta no puede ser nula.");
        }

        // Obtener la lista real desde el repositorio
        List<Solicitud> solicitudes = solicitudRepository.buscarTodos();

        int total = solicitudes.size();
        
        // Calculamos las resueltas (CERRADA / APROBADA / RECHAZADA según tu lógica de negocio)
        long resueltas = solicitudes.stream()
                .filter(s -> s.getEstado() == EstadoEnum.CERRADA || s.getEstado() == EstadoEnum.RECHAZADA)
                .count();

        // Calculamos las pendientes (CREADA / EN_REVISION)
        long pendientes = solicitudes.stream()
                .filter(s -> s.getEstado() == EstadoEnum.CREADA || s.getEstado() == EstadoEnum.EN_REVISION)
                .count();

        // Enviamos los datos generales junto con la lista completa para el desglose por categorías
        return new ReporteResponse(total, resueltas, pendientes, solicitudes);
    }
}