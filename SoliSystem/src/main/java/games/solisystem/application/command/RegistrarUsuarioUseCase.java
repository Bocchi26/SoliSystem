package games.solisystem.application.command;

import games.solisystem.application.dto.RegistrarUsuarioCommand;
import games.solisystem.domain.entity.Usuario;
import games.solisystem.domain.repository.UsuarioRepository;

public class RegistrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public RegistrarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario ejecutar(RegistrarUsuarioCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo.");
        }

        // Validación de infraestructura/aplicación: duplicados en persistencia
        if (usuarioRepository.buscarPorCorreo(command.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        // El constructor de Usuario valida internamente si el nombre, correo o rol son correctos.
        Usuario usuario = new Usuario(command.getNombre(), command.getCorreo(), command.getRol());

        // CORRECCIÓN: Guardar (operación void) y luego retornar el objeto creado
        this.usuarioRepository.guardar(usuario);

        return usuario;
    }
}