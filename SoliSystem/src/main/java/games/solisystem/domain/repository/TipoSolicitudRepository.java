package games.solisystem.domain.repository;

import games.solisystem.domain.entity.TipoSolicitud;
import java.util.List;
import java.util.Optional;

public interface TipoSolicitudRepository {
    void save(TipoSolicitud tipoSolicitud);
    Optional<TipoSolicitud> findById(Long id);
    List<TipoSolicitud> findAll();
}
