package games.solisystem.application.dto;

import games.solisystem.domain.enums.EstadoEnum;

public class CambiarEstadoCommand {
    private final Long solicitudId;
    private final EstadoEnum nuevoEstado;

    public CambiarEstadoCommand(Long solicitudId, EstadoEnum nuevoEstado) {
        this.solicitudId = solicitudId;
        this.nuevoEstado = nuevoEstado;
    }

    public Long getSolicitudId() {
        return solicitudId;
    }

    public EstadoEnum getNuevoEstado() {
        return nuevoEstado;
    }
}