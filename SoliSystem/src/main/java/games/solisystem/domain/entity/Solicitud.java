package games.solisystem.domain.entity;

import games.solisystem.domain.enums.EstadoSolicitud;
import java.time.LocalDate;

public class Solicitud {
    private Long id;
    private Usuario usuario;
    private TipoSolicitud tipoSolicitud;
    private String descripcion;
    private LocalDate fechaCreacion;
    private EstadoSolicitud estado;

    public Solicitud() {}

    public Solicitud(Long id, Usuario usuario, TipoSolicitud tipoSolicitud, String descripcion, LocalDate fechaCreacion, EstadoSolicitud estado) {
        this.id = id;
        this.usuario = usuario;
        this.tipoSolicitud = tipoSolicitud;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public Solicitud(Usuario usuario, TipoSolicitud tipoSolicitud, String descripcion, LocalDate fechaCreacion, EstadoSolicitud estado) {
        this.usuario = usuario;
        this.tipoSolicitud = tipoSolicitud;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoSolicitud getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }
}
