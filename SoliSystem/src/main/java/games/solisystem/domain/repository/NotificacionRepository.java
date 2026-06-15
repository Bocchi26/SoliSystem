package games.solisystem.domain.repository;

import games.solisystem.domain.entity.Notificacion;
import java.util.List;

public interface NotificacionRepository {
    void save(Notificacion notificacion);
    List<Notificacion> findBySolicitudId(Long solicitudId);
    List<Notificacion> findAll();
}
