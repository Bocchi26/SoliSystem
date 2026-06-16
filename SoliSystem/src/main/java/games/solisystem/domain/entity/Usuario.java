package games.solisystem.domain.entity;

import games.solisystem.domain.enums.RolEnum;

public class Usuario {
    private Long id;
    private String nombre;
    private String correo;
    private RolEnum rol;

    public Usuario() {}

    public Usuario(Long id, String nombre, String correo, RolEnum rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public Usuario(String nombre, String correo, RolEnum rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public RolEnum getRol() { return rol; }
    public void setRol(RolEnum rol) { this.rol = rol; }
}
