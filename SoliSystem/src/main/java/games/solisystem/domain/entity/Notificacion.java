package games.solisystem.domain.entity;

import java.time.LocalDate;

import games.solisystem.domain.enums.EstadoEnum;

public class Notificacion {
    private Long id;
    private Solicitud solicitud;
    private String mensaje;
    private LocalDate fecha;
    private String estadoSolicitud;

    public Notificacion() {}

    public Notificacion(Long id, Solicitud solicitud, String mensaje, LocalDate fecha, String estadoSolicitud) {
        this.id = id;
        setSolicitud(solicitud);
        setMensaje(mensaje);
        this.fecha = fecha != null ? fecha : LocalDate.now();
        this.estadoSolicitud = estadoSolicitud;
    }

    // Constructor semántico preferido por el sistema
    public Notificacion(Solicitud solicitud, String mensaje, EstadoEnum estado) {
        this(null, solicitud, mensaje, LocalDate.now(), estado != null ? estado.name() : null);
    }

    public void setSolicitud(Solicitud solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La notificación requiere obligatoriamente una solicitud vinculada.");
        }
        this.solicitud = solicitud;
    }

    public void setMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje de notificación no puede estar vacío.");
        }
        this.mensaje = mensaje.trim();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Solicitud getSolicitud() { return solicitud; }
    public String getMensaje() { return mensaje; }
    public LocalDate getFecha() { return fecha; }
    public String getEstadoSolicitud() { return estadoSolicitud; }
}