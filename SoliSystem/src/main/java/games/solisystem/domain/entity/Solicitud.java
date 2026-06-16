package games.solisystem.domain.entity;

import java.time.LocalDate;

import games.solisystem.domain.enums.EstadoEnum;

public class Solicitud {
    private Long id;
    private Usuario usuario;
    private TipoSolicitud tipoSolicitud;
    private String descripcion;
    private LocalDate fechaCreacion;
    private EstadoEnum estado;

    public Solicitud() {}

    public Solicitud(Long id, Usuario usuario, TipoSolicitud tipoSolicitud, String descripcion, LocalDate fechaCreacion, EstadoEnum estado) {
        this.id = id;
        setUsuario(usuario);
        setTipoSolicitud(tipoSolicitud);
        setDescripcion(descripcion);
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : LocalDate.now();
        this.estado = estado != null ? estado : EstadoEnum.CREADA;
    }

    // Fábrica o Constructor Semántico para Nuevas Solicitudes
    public Solicitud(Usuario usuario, TipoSolicitud tipoSolicitud, String descripcion) {
        this(null, usuario, tipoSolicitud, descripcion, LocalDate.now(), EstadoEnum.CREADA);
    }

    // --- Invariantes y Reglas de Negocio ---

    public void setUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("La solicitud debe estar asociada a un usuario.");
        }
        if (!usuario.puedeCrearSolicitudes()) { // El dominio colabora entre sí
            throw new IllegalArgumentException("Solo un usuario con rol SOLICITANTE puede poseer una solicitud.");
        }
        this.usuario = usuario;
    }

    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
        if (tipoSolicitud == null) {
            throw new IllegalArgumentException("El tipo de solicitud es obligatorio.");
        }
        this.tipoSolicitud = tipoSolicitud;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la solicitud no puede estar vacía.");
        }
        this.descripcion = descripcion.trim();
    }

    public boolean puedeCambiarA(EstadoEnum nuevoEstado) {
        if (this.estado == nuevoEstado) return true;
        
        return (this.estado == EstadoEnum.CREADA && nuevoEstado == EstadoEnum.EN_REVISION) ||
               (this.estado == EstadoEnum.EN_REVISION && (nuevoEstado == EstadoEnum.APROBADA || nuevoEstado == EstadoEnum.RECHAZADA)) ||
               ((this.estado == EstadoEnum.APROBADA || this.estado == EstadoEnum.RECHAZADA) && nuevoEstado == EstadoEnum.CERRADA);
    }

    public void cambiarEstado(EstadoEnum nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo.");
        }
        if (!puedeCambiarA(nuevoEstado)) {
            throw new IllegalArgumentException("Transición no permitida del estado " + this.estado + " al estado " + nuevoEstado);
        }
        this.estado = nuevoEstado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public TipoSolicitud getTipoSolicitud() { return tipoSolicitud; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public EstadoEnum getEstado() { return estado; }
}