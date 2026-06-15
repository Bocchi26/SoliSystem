package games.solisystem.domain.entity;

import java.time.LocalDate;

import games.solisystem.domain.enums.EstadoSolicitud;

public class Notificacion {
    private Long id;
    private Solicitud solicitud;
    private String mensaje;
    private LocalDate fecha;
    private EstadoSolicitud estadoSolicitud;

    public Notificacion(Long id, Solicitud solicitud, String mensaje, LocalDate fecha, EstadoSolicitud estadoSolicitud) {
        this.id = id;
        this.solicitud = solicitud;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.estadoSolicitud = estadoSolicitud;
    }

    public Long getId() { return id; }
    public Solicitud getSolicitud() { return solicitud; }
    public String getMensaje() { return mensaje; }
    public LocalDate getFecha() { return fecha; }
    public EstadoSolicitud getEstadoSolicitud() { return estadoSolicitud; }
}