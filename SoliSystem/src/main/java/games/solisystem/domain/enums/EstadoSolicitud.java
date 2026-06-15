package games.solisystem.domain.enums;

public enum EstadoSolicitud {
    CREADA,
    EN_REVISION,
    APROBADA,
    RECHAZADA,
    CERRADA;

    public boolean esTransicionValida(EstadoSolicitud nuevoEstado) {
        if (this == CERRADA) return false;
        if (this == RECHAZADA && nuevoEstado != CERRADA) return false;
        if (this == APROBADA && nuevoEstado != CERRADA) return false; // Regla de negocio lógica
        return true;
    }
}