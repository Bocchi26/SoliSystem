package games.solisystem.infrastructure.persistence;

import games.solisystem.domain.entity.Notificacion;
import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.entity.TipoSolicitud;
import games.solisystem.domain.entity.Usuario;
import games.solisystem.domain.enums.EstadoSolicitud;
import games.solisystem.domain.enums.RolUsuario;
import games.solisystem.domain.repository.NotificacionRepository;
import games.solisystem.infrastructure.config.DatabaseConfig;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NotificacionRepositoryJdbc implements NotificacionRepository {

    private static final String BASE_SELECT = 
        "SELECT n.id, n.mensaje, n.fecha, n.estado_solicitud, " +
        "       s.id AS s_id, s.descripcion AS s_descripcion, s.fecha_creacion AS s_fecha, s.estado AS s_estado, " +
        "       u.id AS u_id, u.nombre AS u_nombre, u.correo AS u_correo, u.rol AS u_rol, " +
        "       t.id AS t_id, t.nombre AS t_nombre, t.descripcion AS t_descripcion, t.tiempo_estimado_dias AS t_tiempo " +
        "FROM notificaciones n " +
        "JOIN solicitudes s ON n.solicitud_id = s.id " +
        "JOIN usuarios u ON s.usuario_id = u.id " +
        "JOIN tipos_solicitud t ON s.tipo_solicitud_id = t.id";

    @Override
    public void save(Notificacion notificacion) {
        String sql = "INSERT INTO notificaciones (solicitud_id, mensaje, fecha, estado_solicitud) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, notificacion.getSolicitud().getId());
            ps.setString(2, notificacion.getMensaje());
            ps.setDate(3, Date.valueOf(notificacion.getFecha()));
            ps.setString(4, notificacion.getEstadoSolicitud());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    notificacion.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la notificación en la base de datos", e);
        }
    }

    @Override
    public List<Notificacion> findBySolicitudId(Long solicitudId) {
        String sql = BASE_SELECT + " WHERE n.solicitud_id = ? ORDER BY n.fecha DESC, n.id DESC";
        List<Notificacion> notificaciones = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, solicitudId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notificaciones.add(mapRowToNotificacion(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar notificaciones por solicitud ID", e);
        }
        return notificaciones;
    }

    @Override
    public List<Notificacion> findAll() {
        List<Notificacion> notificaciones = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(BASE_SELECT)) {
            while (rs.next()) {
                notificaciones.add(mapRowToNotificacion(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de notificaciones", e);
        }
        return notificaciones;
    }

    private Notificacion mapRowToNotificacion(ResultSet rs) throws SQLException {
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

        Solicitud solicitud = new Solicitud(
            rs.getLong("s_id"),
            usuario,
            tipoSolicitud,
            rs.getString("s_descripcion"),
            rs.getDate("s_fecha").toLocalDate(),
            EstadoSolicitud.valueOf(rs.getString("s_estado"))
        );

        return new Notificacion(
            rs.getLong("id"),
            solicitud,
            rs.getString("mensaje"),
            rs.getDate("fecha").toLocalDate(),
            rs.getString("estado_solicitud")
        );
    }
}
