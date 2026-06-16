package games.solisystem.infrastructure.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import games.solisystem.domain.entity.TipoSolicitud;
import games.solisystem.domain.repository.TipoSolicitudRepository;
import games.solisystem.infrastructure.config.DatabaseConfig;

public class TipoSolicitudRepositoryJdbc implements TipoSolicitudRepository {

    @Override
    public void guardar(TipoSolicitud tipoSolicitud) {
        String sql = "INSERT INTO tipos_solicitud (nombre, descripcion, tiempo_estimado_dias) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tipoSolicitud.getNombre());
            ps.setString(2, tipoSolicitud.getDescripcion());
            ps.setInt(3, tipoSolicitud.getTiempoEstimadoDias());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) tipoSolicitud.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el tipo de solicitud", e);
        }
    }
    
@Override
public Optional<TipoSolicitud> buscarPorNombre(String nombre) {
    String sql = "SELECT id, nombre, descripcion, tiempo_estimado_dias FROM tipos_solicitud WHERE nombre = ?";
    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, nombre);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                TipoSolicitud tipo = new TipoSolicitud(
                    rs.getLong("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getInt("tiempo_estimado_dias")
                );
                return Optional.of(tipo);
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Error al buscar tipo de solicitud por nombre", e);
    }
    return Optional.empty();
}

    @Override
    public Optional<TipoSolicitud> buscarPorId(Long id) {
        String sql = "SELECT id, nombre, descripcion, tiempo_estimado_dias FROM tipos_solicitud WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tipo de solicitud por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<TipoSolicitud> buscarTodos() {
        String sql = "SELECT id, nombre, descripcion, tiempo_estimado_dias FROM tipos_solicitud";
        List<TipoSolicitud> lista = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar tipos de solicitud", e);
        }
        return lista;
    }

    private TipoSolicitud mapRow(ResultSet rs) throws SQLException {
        return new TipoSolicitud(
            rs.getLong("id"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getInt("tiempo_estimado_dias")
        );
    }
}
