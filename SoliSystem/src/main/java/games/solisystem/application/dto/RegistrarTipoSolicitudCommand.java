package games.solisystem.application.dto;

public class RegistrarTipoSolicitudCommand {
    private final String nombre;
    private final String descripcion;
    private final int tiempoEstimadoDias;

    public RegistrarTipoSolicitudCommand(String nombre, String descripcion, int tiempoEstimadoDias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tiempoEstimadoDias = tiempoEstimadoDias;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getTiempoEstimadoDias() {
        return tiempoEstimadoDias;
    }
}