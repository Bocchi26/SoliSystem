package games.solisystem.domain.repository;

import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.enums.EstadoSolicitud;
import java.util.List;
import java.util.Optional;

public interface SolicitudRepository {
    void save(Solicitud solicitud);
    void update(Solicitud solicitud);
    Optional<Solicitud> findById(Long id);
    List<Solicitud> findByEstado(EstadoSolicitud estado);
    List<Solicitud> findAll();
}
