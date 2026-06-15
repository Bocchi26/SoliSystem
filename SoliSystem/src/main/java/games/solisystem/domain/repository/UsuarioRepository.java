package games.solisystem.domain.repository;

import games.solisystem.domain.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    void guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorCorreo(String correo);
    List<Usuario> buscarTodos();
}
