package games.solisystem.application.query;

import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.enums.EstadoSolicitud;
import games.solisystem.domain.repository.SolicitudRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenerarReporteUseCase {
    private final SolicitudRepository solicitudRepo;

    public GenerarReporteUseCase(SolicitudRepository solicitudRepo) {
        this.solicitudRepo = solicitudRepo;
    }

    public void ejecutar() {
        List<Solicitud> todas = solicitudRepo.buscarTodos();
        System.out.println("\n====== REPORTE BÁSICO DE GESTIÓN ======");
        System.out.println("Total de solicitudes radicadas: " + todas.size());
        
        Map<EstadoSolicitud, Long> agrupado = todas.stream()
                .collect(Collectors.groupingBy(Solicitud::getEstado, Collectors.counting()));
        
        for (EstadoSolicitud est : EstadoSolicitud.values()) {
            System.out.println(" - Estado " + est + ": " + agrupado.getOrDefault(est, 0L));
        }
        System.out.println("=======================================");
    }
}