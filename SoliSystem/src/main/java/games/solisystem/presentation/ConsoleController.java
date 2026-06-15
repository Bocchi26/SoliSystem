package games.solisystem.presentation;

import games.solisystem.application.NotificacionObserver;
import games.solisystem.application.command.*;
import games.solisystem.application.dto.*;
import games.solisystem.application.query.*;
import games.solisystem.domain.observer.SolicitudObserver;
import games.solisystem.infrastructure.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleController {
    private final Scanner scanner = new Scanner(System.util.in);
    
    // Cableado manual de dependencias (DI limpia desde la entrada)
    private final UsuarioRepositoryJdbc usuarioRepo = new UsuarioRepositoryJdbc();
    private final TipoSolicitudRepositoryJdbc tipoRepo = new TipoSolicitudRepositoryJdbc();
    private final SolicitudRepositoryJdbc solicitudRepo = new SolicitudRepositoryJdbc();
    private final NotificacionRepositoryJdbc notifRepo = new NotificacionRepositoryJdbc();
    private final List<SolicitudObserver> observers = new ArrayList<>();
    
    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final RegistrarTipoSolicitudUseCase registrarTipoSolicitudUseCase;
    private final CrearSolicitudUseCase crearSolicitudUseCase;
    private final CambiarEstadoSolicitudUseCase cambiarEstadoSolicitudUseCase;
    private final ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase;
    private final GenerarReporteUseCase generarReporteUseCase;

    public ConsoleController() {
        // Inicialización e inscripción del Observer
        observers.add(new NotificacionObserver(notifRepo));
        
        // Inicialización de Casos de Uso pasándoles los Repositorios e Intermediarios (Inversión de Dependencias)
        this.registrarUsuarioUseCase = new RegistrarUsuarioUseCase(usuarioRepo);
        this.registrarTipoSolicitudUseCase = new RegistrarTipoSolicitudUseCase(tipoRepo);
        this.crearSolicitudUseCase = new CrearSolicitudUseCase(solicitudRepo, usuarioRepo, tipoRepo, observers);
        this.cambiarEstadoSolicitudUseCase = new CambiarEstadoSolicitudUseCase(solicitudRepo, observers);
        this.consultarSolicitudesPorEstadoUseCase = new ConsultarSolicitudesPorEstadoUseCase(solicitudRepo);
        this.generarReporteUseCase = new GenerarReporteUseCase(solicitudRepo);
    }

    public void iniciar() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- SOLYSISTEM: MENÚ PRINCIPAL ---");
            System.out.println("1. Registrar Usuario");
            System.out.println("2. Registrar Tipo de Solicitud");
            System.out.println("3. Crear Solicitud");
            System.out.println("4. Cambiar Estado de Solicitud");
            System.out.println("5. Consultar Solicitudes por Estado");
            System.out.println("6. Generar Reporte Básico");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> registrarUsuario();
                    case 2 -> registrarTipoSolicitud();
                    case 3 -> crearSolicitud();
                    case 4 -> cambiarEstado();
                    case 5 -> consultarPorEstado();
                    case 6 -> generarReporteUseCase.ejecutar();
                    case 0 -> System.out.println("Saliendo del sistema...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void registrarUsuario() {
        System.out.print("Nombre: "); String nombre = scanner.nextLine();
        System.out.print("Correo: "); String correo = scanner.nextLine();
        System.out.print("Rol (SOLICITANTE/FUNCIONARIO): "); String rol = scanner.nextLine();
        registrarUsuarioUseCase.ejecutar(new RegistrarUsuarioCommand(nombre, correo, rol));
        System.out.println("Usuario registrado con éxito.");
    }

    private void registrarTipoSolicitud() {
        System.out.print("Nombre del Trámite: "); String nombre = scanner.nextLine();
        System.out.print("Descripción: "); String desc = scanner.nextLine();
        System.out.print("Tiempo estimado (días): "); int dias = Integer.parseInt(scanner.nextLine());
        registrarTipoSolicitudUseCase.ejecutar(new RegistrarTipoSolicitudCommand(nombre, desc, dias));
        System.out.println("Tipo de solicitud guardado.");
    }

    private void crearSolicitud() {
        System.out.print("ID del Usuario Solicitante: "); Long uId = Long.parseLong(scanner.nextLine());
        System.out.print("ID del Tipo de Solicitud: "); Long tId = Long.parseLong(scanner.nextLine());
        System.out.print("Descripción del caso: "); String desc = scanner.nextLine();
        crearSolicitudUseCase.ejecutar(new CrearSolicitudCommand(uId, tId, desc));
        System.out.println("Solicitud creada y registrada correctamente.");
    }

    private void cambiarEstado() {
        System.out.print("ID de la Solicitud: "); Long sId = Long.parseLong(scanner.nextLine());
        System.out.println("Estados válidos: CREADA, EN_REVISION, APROBADA, RECHAZADA, CERRADA");
        System.out.print("Nuevo Estado: "); String estado = scanner.nextLine();
        cambiarEstadoSolicitudUseCase.ejecutar(new CambiarEstadoCommand(sId, estado));
        System.out.println("Transición de estado ejecutada.");
    }

    private void consultarPorEstado() {
        System.out.print("Estado a buscar: "); String estado = scanner.nextLine();
        List<SolicitudResponse> res = consultarSolicitudesPorEstadoUseCase.ejecutar(new ConsultarPorEstadoQuery(estado));
        if(res.isEmpty()) {
            System.out.println("No se encontraron solicitudes con el estado proporcionado.");
        } else {
            res.forEach(System.out.println);
        }
    }
}