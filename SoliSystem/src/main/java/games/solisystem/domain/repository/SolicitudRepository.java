package games.solisystem.domain.repository;
import java.util.List;
import java.util.Optional;

import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.enums.EstadoEnum;

public interface SolicitudRepository {
    void guardar(Solicitud solicitud);
    void actualizar(Solicitud solicitud);
    Optional<Solicitud> buscarPorId(Long id);
    List<Solicitud> buscarPorEstado(EstadoEnum estado);
    List<Solicitud> buscarTodos();

}
