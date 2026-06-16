package games.solisystem.application.query;

import java.util.List;

import games.solisystem.domain.entity.Usuario;
import games.solisystem.domain.repository.UsuarioRepository;

public class ListarUsuariosUseCase {
    private final UsuarioRepository usuarioRepository;

    public ListarUsuariosUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> ejecutar() {
        return usuarioRepository.buscarTodos();
    }
}