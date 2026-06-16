package games.solisystem.domain.repository;

import java.util.List;
import java.util.Optional;

import games.solisystem.domain.entity.TipoSolicitud;

public interface TipoSolicitudRepository {
    void guardar(TipoSolicitud tipoSolicitud);
    Optional<TipoSolicitud> buscarPorId(Long id);
    List<TipoSolicitud> buscarTodos();
    Optional<TipoSolicitud> buscarPorNombre(String nombre);
}
