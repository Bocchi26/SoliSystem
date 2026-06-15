package games.solisystem.domain.repository;

import games.solisystem.domain.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    void save(Usuario usuario);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findAll();
}
