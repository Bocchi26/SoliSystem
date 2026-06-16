package games.solisystem.presentation;

import java.util.List;

import games.solisystem.application.command.CambiarEstadoSolicitudUseCase;
import games.solisystem.application.command.CrearSolicitudUseCase;
import games.solisystem.application.command.RegistrarTipoSolicitudUseCase;
import games.solisystem.application.command.RegistrarUsuarioUseCase;
import games.solisystem.application.query.ConsultarSolicitudesPorEstadoUseCase;
import games.solisystem.application.query.GenerarReporteUseCase;
import games.solisystem.application.query.ListarTiposSolicitudUseCase;
import games.solisystem.application.query.ListarTodasLasSolicitudesUseCase;
import games.solisystem.application.query.ListarUsuariosUseCase;
import games.solisystem.domain.observer.SolicitudObserver;
import games.solisystem.domain.repository.NotificacionRepository;
import games.solisystem.domain.repository.SolicitudRepository;
import games.solisystem.domain.repository.TipoSolicitudRepository;
import games.solisystem.domain.repository.UsuarioRepository;
import games.solisystem.infrastructure.config.DatabaseConfig;
import games.solisystem.infrastructure.observer.NotificacionObserver;
import games.solisystem.infrastructure.persistence.NotificacionRepositoryJdbc;
import games.solisystem.infrastructure.persistence.SolicitudRepositoryJdbc;
import games.solisystem.infrastructure.persistence.TipoSolicitudRepositoryJdbc;
import games.solisystem.infrastructure.persistence.UsuarioRepositoryJdbc;

public class Main {
    public static void main(String[] args) {
        
        DatabaseConfig.initializeDatabase();

        UsuarioRepository usuarioRepo = new UsuarioRepositoryJdbc();
        TipoSolicitudRepository tipoSolicitudRepo = new TipoSolicitudRepositoryJdbc();
        SolicitudRepository solicitudRepo = new SolicitudRepositoryJdbc();
        NotificacionRepository notificacionRepo = new NotificacionRepositoryJdbc();

        SolicitudObserver notificacionObserver = new NotificacionObserver(notificacionRepo);
        List<SolicitudObserver> observers = List.of(notificacionObserver);

        // CASOS DE USO - COMANDOS
        RegistrarUsuarioUseCase registrarUsuario = new RegistrarUsuarioUseCase(usuarioRepo);
        RegistrarTipoSolicitudUseCase registrarTipo = new RegistrarTipoSolicitudUseCase(tipoSolicitudRepo);
        CrearSolicitudUseCase crearSolicitud = new CrearSolicitudUseCase(solicitudRepo, usuarioRepo, tipoSolicitudRepo, observers);
        CambiarEstadoSolicitudUseCase cambiarEstado = new CambiarEstadoSolicitudUseCase(solicitudRepo, observers);

        // CASOS DE USO - CONSULTAS
        ConsultarSolicitudesPorEstadoUseCase consultarPorEstado = new ConsultarSolicitudesPorEstadoUseCase(solicitudRepo);
        GenerarReporteUseCase generarReporte = new GenerarReporteUseCase(solicitudRepo);
        ListarUsuariosUseCase listarUsuarios = new ListarUsuariosUseCase(usuarioRepo);
        ListarTiposSolicitudUseCase listarTipos = new ListarTiposSolicitudUseCase(tipoSolicitudRepo);
        ListarTodasLasSolicitudesUseCase listarTodas = new ListarTodasLasSolicitudesUseCase(solicitudRepo);

        // CONTRUCTOR TOTALMENTE LIMPIO DE RESPONSABILIDADES DE INFRAESTRUCTURA
        ConsoleController controller = new ConsoleController(
                registrarUsuario,
                registrarTipo,
                crearSolicitud,
                cambiarEstado,
                consultarPorEstado,
                generarReporte,
                listarUsuarios,
                listarTipos,
                listarTodas
        );

        controller.iniciar();
    }
}