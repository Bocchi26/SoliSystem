package games.solisystem.domain.entity;

public class TipoSolicitud {
    private Long id;
    private String nombre;
    private String descripcion;
    private int tiempoEstimadoDias;

    public TipoSolicitud() {}

    public TipoSolicitud(Long id, String nombre, String descripcion, int tiempoEstimadoDias) {
        setId(id);
        setNombre(nombre);
        setDescripcion(descripcion);
        setTiempoEstimadoDias(tiempoEstimadoDias);
    }

    public TipoSolicitud(String nombre, String descripcion, int tiempoEstimadoDias) {
        this(null, nombre, descripcion, tiempoEstimadoDias);
    }

    // --- Invariantes de Dominio ---

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del tipo de solicitud es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del tipo de solicitud es obligatoria.");
        }
        this.descripcion = descripcion.trim();
    }

    public void setTiempoEstimadoDias(int tiempoEstimadoDias) {
        if (tiempoEstimadoDias <= 0) {
            throw new IllegalArgumentException("El tiempo estimado debe ser mayor a cero días.");
        }
        this.tiempoEstimadoDias = tiempoEstimadoDias;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getTiempoEstimadoDias() { return tiempoEstimadoDias; }
}