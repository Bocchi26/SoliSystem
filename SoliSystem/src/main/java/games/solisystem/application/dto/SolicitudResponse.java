package games.solisystem.application.dto;

import games.solisystem.domain.entity.Solicitud;

public class SolicitudResponse {
    public Long id;
    public String solicitante;
    public String tipo;
    public String descripcion;
    public String fecha;
    public String estado;

    public SolicitudResponse(Solicitud s) {
        this.id = s.getId();
        this.solicitante = s.getUsuario().getNombre();
        this.tipo = s.getTipoSolicitud().getNombre();
        this.descripcion = s.getDescripcion();
        this.fecha = s.getFechaCreacion().toString();
        this.estado = s.getEstado().name();
    }

    @Override
    public String toString() {
        return String.format("[%d] %s | Tipo: %s | Estado: %s | Descripción: %s (%s)", id, solicitante, tipo, estado, descripcion, fecha);
    }
}