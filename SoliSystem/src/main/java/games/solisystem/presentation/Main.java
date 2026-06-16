package games.solisystem.presentation;

import java.util.List;

import games.solisystem.application.command.CambiarEstadoSolicitudUseCase;
import games.solisystem.application.command.CrearSolicitudUseCase;
import games.solisystem.application.command.RegistrarTipoSolicitudUseCase;
import games.solisystem.application.command.RegistrarUsuarioUseCase;
import games.solisystem.application.query.ConsultarSolicitudesPorEstadoUseCase;
import games.solisystem.application.query.GenerarReporteUseCase;
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

/**
 * CAPA: Presentation — punto de entrada de la aplicación.
 *
 * Responsabilidad única: construir el grafo de dependencias (Composition Root)
 * e iniciar el controlador. Toda la inyección manual de dependencias ocurre aquí,
 * demostrando el patrón Dependency Injection sin framework externo.
 *
 * Flujo Clean Architecture:
 *   Main → ConsoleController → Use Cases → Domain interfaces
 *                                        ↑
 *                              Infrastructure implements
 */
public class Main {

    public static void main(String[] args) {
        DatabaseConfig.initializeDatabase();

        // ── 1. Infraestructura: repositorios concretos (JDBC) ────────────────
        //    Las interfaces viven en domain; las implementaciones aquí.
        //    Cambiar de PostgreSQL a H2 solo requiere cambiar estas líneas.
        UsuarioRepository       usuarioRepo       = new UsuarioRepositoryJdbc();
        TipoSolicitudRepository tipoSolicitudRepo = new TipoSolicitudRepositoryJdbc();
        SolicitudRepository     solicitudRepo     = new SolicitudRepositoryJdbc();
        NotificacionRepository  notificacionRepo  = new NotificacionRepositoryJdbc();

        // ── 2. Patrón Observer (GoF) ─────────────────────────────────────────
        //    NotificacionObserver es el observador concreto.
        //    Los casos de uso solo conocen la interfaz SolicitudObserver.
        SolicitudObserver notificacionObserver = new NotificacionObserver(notificacionRepo);
        List<SolicitudObserver> observers = List.of(notificacionObserver);

        // ── 3. Capa Application — Comandos (CQRS: escritura) ─────────────────
        RegistrarUsuarioUseCase registrarUsuario =
                new RegistrarUsuarioUseCase(usuarioRepo);

        RegistrarTipoSolicitudUseCase registrarTipo =
                new RegistrarTipoSolicitudUseCase(tipoSolicitudRepo);

        CrearSolicitudUseCase crearSolicitud =
        new CrearSolicitudUseCase(
                solicitudRepo,
                usuarioRepo,
                tipoSolicitudRepo,
                observers); 

        CambiarEstadoSolicitudUseCase cambiarEstado =
                new CambiarEstadoSolicitudUseCase(
                        solicitudRepo,
                        
                        observers);

        // ── 4. Capa Application — Consultas (CQRS: lectura) ──────────────────
        ConsultarSolicitudesPorEstadoUseCase consultarPorEstado =
                new ConsultarSolicitudesPorEstadoUseCase(solicitudRepo);

        GenerarReporteUseCase generarReporte =
                new GenerarReporteUseCase(solicitudRepo);

        // ── 5. Capa Presentation — controlador de consola ────────────────────
        //    Recibe todos los casos de uso por constructor.
        //    No conoce ningún repositorio directamente.
        ConsoleController controller = new ConsoleController(
        registrarUsuario,
        registrarTipo,
        crearSolicitud,
        cambiarEstado,
        consultarPorEstado,
        generarReporte,
        usuarioRepo,          // ← nuevo
        tipoSolicitudRepo,    // ← nuevo
        solicitudRepo);       // ← nuevo

        // ── 6. Iniciar la aplicación ─────────────────────────────────────────
        controller.iniciar();
    }
}