package games.solisystem.domain.entity;

import games.solisystem.domain.enums.EstadoEnum;
import java.time.LocalDate;

public class Notificacion {
    private Long id;
    private Solicitud solicitud;
    private String mensaje;
    private LocalDate fecha;
    private String estadoSolicitud;

    public Notificacion() {}

    public Notificacion(Long id, Solicitud solicitud, String mensaje, LocalDate fecha, String estadoSolicitud) {
        this.id = id;
        this.solicitud = solicitud;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.estadoSolicitud = estadoSolicitud;
    }

    // Constructor que acepta EstadoEnum directamente (convierte a String)
    public Notificacion(Long id, Solicitud solicitud, String mensaje, LocalDate fecha, EstadoEnum estado) {
        this.id = id;
        this.solicitud = solicitud;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.estadoSolicitud = estado != null ? estado.name() : null;
    }

    public Notificacion(Solicitud solicitud, String mensaje, LocalDate fecha, String estadoSolicitud) {
        this.solicitud = solicitud;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.estadoSolicitud = estadoSolicitud;
    }

    public Notificacion(Solicitud solicitud, String mensaje, LocalDate fecha, EstadoEnum estado) {
        this.solicitud = solicitud;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.estadoSolicitud = estado != null ? estado.name() : null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Solicitud getSolicitud() { return solicitud; }
    public void setSolicitud(Solicitud solicitud) { this.solicitud = solicitud; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getEstadoSolicitud() { return estadoSolicitud; }
    public void setEstadoSolicitud(String estadoSolicitud) { this.estadoSolicitud = estadoSolicitud; }
}
