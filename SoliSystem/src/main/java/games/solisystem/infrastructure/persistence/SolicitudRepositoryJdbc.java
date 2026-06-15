package games.solisystem.infrastructure.persistence;

import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.entity.TipoSolicitud;
import games.solisystem.domain.entity.Usuario;
import games.solisystem.domain.enums.EstadoSolicitud;
import games.solisystem.domain.enums.RolUsuario;
import games.solisystem.domain.repository.SolicitudRepository;
import games.solisystem.infrastructure.config.DatabaseConfig;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SolicitudRepositoryJdbc implements SolicitudRepository {

    private static final String BASE_SELECT = 
        "SELECT s.id, s.descripcion, s.fecha_creacion, s.estado, " +
        "       u.id AS u_id, u.nombre AS u_nombre, u.correo AS u_correo, u.rol AS u_rol, " +
        "       t.id AS t_id, t.nombre AS t_nombre, t.descripcion AS t_descripcion, t.tiempo_estimado_dias AS t_tiempo " +
        "FROM solicitudes s " +
        "JOIN usuarios u ON s.usuario_id = u.id " +
        "JOIN tipos_solicitud t ON s.tipo_solicitud_id = t.id";

    @Override
    public void save(Solicitud solicitud) {
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
                if (rs.next()) {
                    solicitud.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la solicitud en la base de datos", e);
        }
    }

    @Override
    public void update(Solicitud solicitud) {
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
            throw new RuntimeException("Error al actualizar la solicitud en la base de datos", e);
        }
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        String sql = BASE_SELECT + " WHERE s.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToSolicitud(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la solicitud por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Solicitud> findByEstado(EstadoSolicitud estado) {
        String sql = BASE_SELECT + " WHERE s.estado = ?";
        List<Solicitud> solicitudes = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    solicitudes.add(mapRowToSolicitud(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar solicitudes por estado", e);
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findAll() {
        List<Solicitud> solicitudes = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(BASE_SELECT)) {
            while (rs.next()) {
                solicitudes.add(mapRowToSolicitud(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de solicitudes", e);
        }
        return solicitudes;
    }

    private Solicitud mapRowToSolicitud(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario(
            rs.getLong("u_id"),
            rs.getString("u_nombre"),
            rs.getString("u_correo"),
            RolUsuario.valueOf(rs.getString("u_rol"))
        );

        TipoSolicitud tipoSolicitud = new TipoSolicitud(
            rs.getLong("t_id"),
            rs.getString("t_nombre"),
            rs.getString("t_descripcion"),
            rs.getInt("t_tiempo")
        );

        return new Solicitud(
            rs.getLong("id"),
            usuario,
            tipoSolicitud,
            rs.getString("descripcion"),
            rs.getDate("fecha_creacion").toLocalDate(),
            EstadoSolicitud.valueOf(rs.getString("estado"))
        );
    }
}
