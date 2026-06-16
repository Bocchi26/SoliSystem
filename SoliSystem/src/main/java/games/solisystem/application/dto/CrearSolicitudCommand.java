package games.solisystem.application.dto;

public class CrearSolicitudCommand {
    private final Long usuarioId;
    private final Long tipoSolicitudId;
    private final String descripcion;

    public CrearSolicitudCommand(Long usuarioId, Long tipoSolicitudId, String descripcion) {
        this.usuarioId = usuarioId;
        this.tipoSolicitudId = tipoSolicitudId;
        this.descripcion = descripcion;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getTipoSolicitudId() {
        return tipoSolicitudId;
    }

    public String getDescripcion() {
        return descripcion;
    }
}