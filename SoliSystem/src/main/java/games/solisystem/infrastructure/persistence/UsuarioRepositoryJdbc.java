package games.solisystem.infrastructure.persistence;

import games.solisystem.domain.entity.Usuario;
import games.solisystem.domain.enums.RolUsuario;
import games.solisystem.domain.repository.UsuarioRepository;
import games.solisystem.infrastructure.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositoryJdbc implements UsuarioRepository {

    @Override
    public void save(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, rol) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getRol().name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el usuario en la base de datos", e);
        }
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        String sql = "SELECT id, nombre, correo, rol FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el usuario por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        String sql = "SELECT id, nombre, correo, rol FROM usuarios WHERE correo = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el usuario por correo", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> findAll() {
        String sql = "SELECT id, nombre, correo, rol FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(mapRowToUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de usuarios", e);
        }
        return usuarios;
    }

    private Usuario mapRowToUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getLong("id"),
            rs.getString("nombre"),
            rs.getString("correo"),
            RolUsuario.valueOf(rs.getString("rol"))
        );
    }
}
