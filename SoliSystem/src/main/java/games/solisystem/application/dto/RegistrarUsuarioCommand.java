package games.solisystem.application.dto;

import games.solisystem.domain.enums.RolEnum;

public class RegistrarUsuarioCommand {
    private final String nombre;
    private final String correo;
    private final RolEnum rol;

    public RegistrarUsuarioCommand(String nombre, String correo, RolEnum rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public RolEnum getRol() {
        return rol;
    }
}