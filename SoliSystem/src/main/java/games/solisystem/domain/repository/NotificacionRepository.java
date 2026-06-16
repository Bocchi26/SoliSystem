package games.solisystem.domain.repository;

import games.solisystem.domain.entity.Notificacion;
import java.util.List;

public interface NotificacionRepository {
    void guardar(Notificacion notificacion);
    List<Notificacion> buscarPorSolicitudId(Long solicitudId);
    List<Notificacion> buscarTodos();
}
