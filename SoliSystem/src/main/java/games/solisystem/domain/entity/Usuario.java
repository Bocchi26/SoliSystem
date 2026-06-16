package games.solisystem.domain.entity;

import games.solisystem.domain.enums.RolEnum;

public class Usuario {
    private Long id;
    private String nombre;
    private String correo;
    private RolEnum rol;

    public Usuario() {}

    public Usuario(Long id, String nombre, String correo, RolEnum rol) {
        setId(id);
        setNombre(nombre);
        setCorreo(correo);
        setRol(rol);
    }

    public Usuario(String nombre, String correo, RolEnum rol) {
        this(null, nombre, correo, rol);
    }

    // --- Reglas de negocio embebidas en el Dominio ---
    
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public void setCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo del usuario no puede estar vacío.");
        }
        if (!correo.contains("@")) { // Validación básica intrínseca al formato del dominio
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }
        this.correo = correo.trim();
    }

    public void setRol(RolEnum rol) {
        if (rol == null) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio.");
        }
        this.rol = rol;
    }

    // Comportamiento de Dominio (Evita preguntar el rol afuera; le preguntamos a la entidad)
    public boolean puedeCrearSolicitudes() {
        return this.rol == RolEnum.SOLICITANTE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public RolEnum getRol() { return rol; }
}