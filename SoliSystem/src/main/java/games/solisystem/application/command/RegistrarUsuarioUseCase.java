package games.solisystem.application.command;

import games.solisystem.application.dto.RegistrarUsuarioCommand;
import games.solisystem.domain.entity.Usuario;
import games.solisystem.domain.enums.RolEnum;
import games.solisystem.domain.repository.UsuarioRepository;

public class RegistrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public RegistrarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario ejecutar(RegistrarUsuarioCommand command) {
        validar(command);

        if (usuarioRepository.buscarPorCorreo(command.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya existe en el sistema.");
        }

        Usuario usuario = new Usuario(
                null,
                command.getNombre().trim(),
                command.getCorreo().trim(),
                command.getRol());

        usuarioRepository.guardar(usuario);
        return usuario;
    }

    private void validar(RegistrarUsuarioCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo.");
        }
        if (command.getNombre() == null || command.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (command.getCorreo() == null || command.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
        if (command.getRol() == null) {
            throw new IllegalArgumentException("El rol es obligatorio.");
        }
        if (command.getRol() != RolEnum.SOLICITANTE && command.getRol() != RolEnum.FUNCIONARIO) {
            throw new IllegalArgumentException("El rol no es vÃ¡lido.");
        }
    }
}