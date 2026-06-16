package games.solisystem.infrastructure.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.entity.TipoSolicitud;
import games.solisystem.domain.entity.Usuario;
import games.solisystem.domain.enums.EstadoEnum;
import games.solisystem.domain.enums.RolEnum;
import games.solisystem.domain.repository.SolicitudRepository;
import games.solisystem.infrastructure.config.DatabaseConfig;

public class SolicitudRepositoryJdbc implements SolicitudRepository {

    private static final String BASE_SELECT =
        "SELECT s.id, s.descripcion, s.fecha_creacion, s.estado, " +
        "       u.id AS u_id, u.nombre AS u_nombre, u.correo AS u_correo, u.rol AS u_rol, " +
        "       t.id AS t_id, t.nombre AS t_nombre, t.descripcion AS t_descripcion, t.tiempo_estimado_dias AS t_tiempo " +
        "FROM solicitudes s " +
        "JOIN usuarios u ON s.usuario_id = u.id " +
        "JOIN tipos_solicitud t ON s.tipo_solicitud_id = t.id";

    @Override
    public void guardar(Solicitud solicitud) {
        String sql = "INSERT INTO solicitudes (usuario_id, tipo_solicitud_id, descripcion, fecha_creacion, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, solicitud.getUsuario().getId());
            ps.setLong(2, solicitud.getTipoSolicitud().getId());
            ps.setString(3, solicitud.getDescripcion());
            ps.setDate(4, Date.valueOf(solicitud.getFechaCreacion()));
            ps.setString(5, solicitud.getEstado().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) solicitud.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la solicitud", e);
        }
    }
    
    @Override
    public void actualizar(Solicitud solicitud) {
        String sql = "UPDATE solicitudes SET usuario_id = ?, tipo_solicitud_id = ?, descripcion = ?, fecha_creacion = ?, estado = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, solicitud.getUsuario().getId());
            ps.setLong(2, solicitud.getTipoSolicitud().getId());
            ps.setString(3, solicitud.getDescripcion());
            ps.setDate(4, Date.valueOf(solicitud.getFechaCreacion()));
            ps.setString(5, solicitud.getEstado().name());
            ps.setLong(6, solicitud.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la solicitud", e);
        }
    }

    @Override
    public Optional<Solicitud> buscarPorId(Long id) {
        String sql = BASE_SELECT + " WHERE s.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar solicitud por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Solicitud> buscarPorEstado(EstadoEnum estado) {
        String sql = BASE_SELECT + " WHERE s.estado = ?";
        List<Solicitud> lista = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar solicitudes por estado", e);
        }
        return lista;
    }

    @Override
    public List<Solicitud> buscarTodos() {
        List<Solicitud> lista = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(BASE_SELECT)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar solicitudes", e);
        }
        return lista;
    }

    private Solicitud mapRow(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario(
            rs.getLong("u_id"),
            rs.getString("u_nombre"),
            rs.getString("u_correo"),
            RolEnum.valueOf(rs.getString("u_rol"))
        );
        TipoSolicitud tipo = new TipoSolicitud(
            rs.getLong("t_id"),
            rs.getString("t_nombre"),
            rs.getString("t_descripcion"),
            rs.getInt("t_tiempo")
        );
        return new Solicitud(
            rs.getLong("id"),
            usuario,
            tipo,
            rs.getString("descripcion"),
            rs.getDate("fecha_creacion").toLocalDate(),
            EstadoEnum.valueOf(rs.getString("estado"))
        );
    }
}
