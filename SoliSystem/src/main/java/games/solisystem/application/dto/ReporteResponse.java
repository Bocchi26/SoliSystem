package games.solisystem.application.dto;

import java.util.List;

import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.enums.EstadoEnum;

public class ReporteResponse {
    private final int totalSolicitudes;
    private final long solicitudesResueltas;
    private final long solicitudesPendientes;
    private final List<Solicitud> todasLasSolicitudes;

    public ReporteResponse(int totalSolicitudes, long solicitudesResueltas, long solicitudesPendientes, List<Solicitud> todasLasSolicitudes) {
        this.totalSolicitudes = totalSolicitudes;
        this.solicitudesResueltas = solicitudesResueltas;
        this.solicitudesPendientes = solicitudesPendientes;
        this.todasLasSolicitudes = todasLasSolicitudes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n--- RESUMEN GENERAL ---\n");
        sb.append("Total solicitudes: ").append(totalSolicitudes).append("\n");
        sb.append("Resueltas: ").append(solicitudesResueltas).append("\n");
        sb.append("Pendientes: ").append(solicitudesPendientes).append("\n");
        
        sb.append("\n--- SOLICITUDES POR ESTADO ---\n");
        for (EstadoEnum estado : EstadoEnum.values()) {
            List<Solicitud> filtradas = todasLasSolicitudes.stream()
                    .filter(s -> s.getEstado() == estado)
                    .toList();

            sb.append("[").append(estado).append("] (").append(filtradas.size()).append(")\n");
            
            if (filtradas.isEmpty()) {
                sb.append("  No hay solicitudes.\n");
            } else {
                for (Solicitud s : filtradas) {
                    sb.append("  ID: ").append(s.getId())
                      .append(" | Tipo: ").append(s.getTipoSolicitud().getNombre())
                      .append(" | Usuario: ").append(s.getUsuario().getNombre())
                      .append("\n");
                }
            }
        }
        return sb.toString();
    }
}