package games.solisystem.application.dto;

import games.solisystem.domain.enums.EstadoEnum;

import java.time.LocalDate;

public class SolicitudResponse {

    private Long id;
    private String usuario;
    private String tipoSolicitud;
    private String descripcion;
    private LocalDate fechaCreacion;
    private EstadoEnum estado;

    public SolicitudResponse(Long id, String usuario, String tipoSolicitud,
            String descripcion, LocalDate fechaCreacion, EstadoEnum estado) {
        this.id = id;
        this.usuario = usuario;
        this.tipoSolicitud = tipoSolicitud;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public EstadoEnum getEstado() {
        return estado;
    }
}