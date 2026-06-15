package games.solisystem.domain.entity;

import java.time.LocalDate;

import games.solisystem.domain.enums.EstadoSolicitud;

public class Solicitud {
    private Long id;
    private Usuario usuario;
    private TipoSolicitud tipoSolicitud;
    private String descripcion;
    private LocalDate fechaCreacion;
    private EstadoSolicitud estado;

    public Solicitud(Long id, Usuario usuario, TipoSolicitud tipoSolicitud, String descripcion, LocalDate fechaCreacion, EstadoSolicitud estado) {
        this.id = id;
        this.usuario = usuario;
        this.tipoSolicitud = tipoSolicitud;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public void cambiarEstado(EstadoSolicitud nuevoEstado) {
        if (!this.estado.esTransicionValida(nuevoEstado)) {
            throw new IllegalStateException("Transición de estado no permitida desde " + this.estado + " a " + nuevoEstado);
        }
        this.estado = nuevoEstado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public TipoSolicitud getTipoSolicitud() { return tipoSolicitud; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public EstadoSolicitud getEstado() { return estado; }
}