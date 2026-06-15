package games.solisystem.domain.entity;

import games.solisystem.domain.enums.RolUsuario;

public class Usuario {
    private Long id;
    private String nombre;
    private String correo;
    private RolUsuario rol;

    public Usuario(Long id, String nombre, String correo, RolUsuario rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public RolUsuario getRol() { return rol; }
}