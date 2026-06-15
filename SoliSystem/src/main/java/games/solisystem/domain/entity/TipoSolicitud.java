package games.solisystem.domain.entity;

public class TipoSolicitud {
    private Long id;
    private String nombre;
    private String descripcion;
    private int tiempoEstimadoDias;

    public TipoSolicitud() {}

    public TipoSolicitud(Long id, String nombre, String descripcion, int tiempoEstimadoDias) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tiempoEstimadoDias = tiempoEstimadoDias;
    }

    public TipoSolicitud(String nombre, String descripcion, int tiempoEstimadoDias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tiempoEstimadoDias = tiempoEstimadoDias;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTiempoEstimadoDias() {
        return tiempoEstimadoDias;
    }

    public void setTiempoEstimadoDias(int tiempoEstimadoDias) {
        this.tiempoEstimadoDias = tiempoEstimadoDias;
    }
}
