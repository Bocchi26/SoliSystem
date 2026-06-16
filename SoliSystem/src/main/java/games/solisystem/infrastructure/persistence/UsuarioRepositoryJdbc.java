package games.solisystem.infrastructure.persistence;

import games.solisystem.domain.entity.Usuario;
import games.solisystem.domain.enums.RolEnum;
import games.solisystem.domain.repository.UsuarioRepository;
import games.solisystem.infrastructure.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositoryJdbc implements UsuarioRepository {

    @Override
    public void guardar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, rol) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getRol().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) usuario.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el usuario", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        String sql = "SELECT id, nombre, correo, rol FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        String sql = "SELECT id, nombre, correo, rol FROM usuarios WHERE correo = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por correo", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> buscarTodos() {
        String sql = "SELECT id, nombre, correo, rol FROM usuarios";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios", e);
        }
        return lista;
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getLong("id"),
            rs.getString("nombre"),
            rs.getString("correo"),
            RolEnum.valueOf(rs.getString("rol"))
        );
    }
}
