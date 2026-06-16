package games.solisystem.domain.repository;

import games.solisystem.domain.entity.TipoSolicitud;
import java.util.List;
import java.util.Optional;

public interface TipoSolicitudRepository {
    void guardar(TipoSolicitud tipoSolicitud);
    Optional<TipoSolicitud> buscarPorId(Long id);
    List<TipoSolicitud> buscarTodos();
}
