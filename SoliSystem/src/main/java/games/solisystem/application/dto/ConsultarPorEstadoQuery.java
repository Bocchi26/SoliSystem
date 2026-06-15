package games.solisystem.application.dto;

import games.solisystem.domain.enums.EstadoEnum;

public class ConsultarPorEstadoQuery {
    private final EstadoEnum estado;

    public ConsultarSolicitudesPorEstadoQuery(EstadoEnum estado) {
        this.estado = estado;
    }

    public EstadoEnum getEstado() {
        return estado;
    }
}