package games.solisystem.presentation;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

import games.solisystem.application.command.CambiarEstadoSolicitudUseCase;
import games.solisystem.application.command.CrearSolicitudUseCase;
import games.solisystem.application.command.RegistrarTipoSolicitudUseCase;
import games.solisystem.application.command.RegistrarUsuarioUseCase;
import games.solisystem.application.dto.CambiarEstadoCommand;
import games.solisystem.application.dto.ConsultarPorEstadoQuery;
import games.solisystem.application.dto.CrearSolicitudCommand;
import games.solisystem.application.dto.GenerarReporteQuery;
import games.solisystem.application.dto.RegistrarTipoSolicitudCommand;
import games.solisystem.application.dto.RegistrarUsuarioCommand;
import games.solisystem.application.dto.ReporteResponse;
import games.solisystem.application.query.ConsultarSolicitudesPorEstadoUseCase;
import games.solisystem.application.query.GenerarReporteUseCase;
import games.solisystem.application.query.ListarTiposSolicitudUseCase;
import games.solisystem.application.query.ListarTodasLasSolicitudesUseCase;
import games.solisystem.application.query.ListarUsuariosUseCase;
import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.entity.TipoSolicitud;
import games.solisystem.domain.entity.Usuario;
import games.solisystem.domain.enums.EstadoEnum;
import games.solisystem.domain.enums.RolEnum;

public class ConsoleController {

    private static final Pattern SOLO_LETRAS = Pattern.compile("^[\\p{L} .'-]+$");
    private static final Pattern CORREO_VALIDO = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern NOMBRE_TIPO = Pattern.compile("^[\\p{L}\\p{N} .,-]+$");

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final RegistrarTipoSolicitudUseCase registrarTipoSolicitudUseCase;
    private final CrearSolicitudUseCase crearSolicitudUseCase;
    private final CambiarEstadoSolicitudUseCase cambiarEstadoSolicitudUseCase;
    private final ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase;
    private final GenerarReporteUseCase generarReporteUseCase;
    
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final ListarTiposSolicitudUseCase listarTiposSolicitudUseCase;
    private final ListarTodasLasSolicitudesUseCase listarTodasLasSolicitudesUseCase;

    private final Scanner scanner;
    private Usuario usuarioAutenticado;

    public ConsoleController(
            RegistrarUsuarioUseCase registrarUsuarioUseCase,
            RegistrarTipoSolicitudUseCase registrarTipoSolicitudUseCase,
            CrearSolicitudUseCase crearSolicitudUseCase,
            CambiarEstadoSolicitudUseCase cambiarEstadoSolicitudUseCase,
            ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase,
            GenerarReporteUseCase generarReporteUseCase,
            ListarUsuariosUseCase listarUsuariosUseCase,
            ListarTiposSolicitudUseCase listarTiposSolicitudUseCase,
            ListarTodasLasSolicitudesUseCase listarTodasLasSolicitudesUseCase) {
        this.registrarUsuarioUseCase = registrarUsuarioUseCase;
        this.registrarTipoSolicitudUseCase = registrarTipoSolicitudUseCase;
        this.crearSolicitudUseCase = crearSolicitudUseCase;
        this.cambiarEstadoSolicitudUseCase = cambiarEstadoSolicitudUseCase;
        this.consultarSolicitudesPorEstadoUseCase = consultarSolicitudesPorEstadoUseCase;
        this.generarReporteUseCase = generarReporteUseCase;
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.listarTiposSolicitudUseCase = listarTiposSolicitudUseCase;
        this.listarTodasLasSolicitudesUseCase = listarTodasLasSolicitudesUseCase;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            if (usuarioAutenticado == null) {
                identificarUsuario();
                continue;
            }

            if (usuarioAutenticado.getRol() == RolEnum.SOLICITANTE) {
                salir = mostrarMenuSolicitante();
            } else if (usuarioAutenticado.getRol() == RolEnum.FUNCIONARIO) {
                salir = mostrarMenuFuncionario();
            }
        }
    }

    private void identificarUsuario() {
        System.out.println("\n--- SOLISYSTEM ---");
        System.out.print("Ingrese ID de usuario (o escriba 'crear' para registrarse): ");
        String entrada = scanner.nextLine().trim();

        if (entrada.equalsIgnoreCase("crear")) {
            registrarUsuario();
            return;
        }

        try {
            Long id = Long.parseLong(entrada);
            Optional<Usuario> usuarioOpt = listarUsuariosUseCase.ejecutar().stream()
                    .filter(u -> u.getId().equals(id))
                    .findFirst();

            if (usuarioOpt.isPresent()) {
                this.usuarioAutenticado = usuarioOpt.get();
                System.out.println("Sesión iniciada: " + usuarioAutenticado.getNombre() + " (" + usuarioAutenticado.getRol() + ")");
            } else {
                System.out.println("Error: El ID de usuario no existe.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un identificador numérico válido.");
        }
    }

    private boolean mostrarMenuSolicitante() {
        System.out.println("\n[ MENU SOLICITANTE | Sesión: " + usuarioAutenticado.getNombre() + " ]");
        System.out.println("1. Crear solicitud");
        System.out.println("2. Consultar mis solicitudes por estado");
        System.out.println("3. Cerrar sesión");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");

        String opcion = scanner.nextLine().trim();
        switch (opcion) {
            case "1" -> crearSolicitud();
            case "2" -> consultarPorEstado();
            case "3" -> cerrarSesion();
            case "0" -> { return true; }
            default -> System.out.println("Opción no válida.");
        }
        return false;
    }

    private boolean mostrarMenuFuncionario() {
        System.out.println("\n[ MENÚ FUNCIONARIO | Sesión: " + usuarioAutenticado.getNombre() + " ]");
        System.out.println("1. Registrar tipo de solicitud");
        System.out.println("2. Cambiar estado de una solicitud");
        System.out.println("3. Consultar solicitudes por estado");
        System.out.println("4. Ver reporte estadístico");
        System.out.println("5. Registrar nuevo usuario");
        System.out.println("6. Cerrar sesión");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");

        String opcion = scanner.nextLine().trim();
        switch (opcion) {
            case "1" -> registrarTipoSolicitud();
            case "2" -> cambiarEstado();
            case "3" -> consultarPorEstado();
            case "4" -> generarReporte();
            case "5" -> registrarUsuario();
            case "6" -> cerrarSesion();
            case "0" -> { return true; }
            default -> System.out.println("Opción no válida.");
        }
        return false;
    }

    private void cerrarSesion() {
        this.usuarioAutenticado = null;
        System.out.println("Sesión finalizada.");
    }

    private void crearSolicitud() {
        if (usuarioAutenticado.getRol() != RolEnum.SOLICITANTE) {
            System.out.println("Acceso denegado: Operación exclusiva para solicitantes.");
            return;
        }

        List<TipoSolicitud> tipos = listarTiposSolicitudUseCase.ejecutar();
        if (tipos.isEmpty()) {
            System.out.println("No hay tipos de solicitud disponibles en el sistema.");
            return;
        }

        System.out.println("\nTipos de solicitud:");
        for (int i = 0; i < tipos.size(); i++) {
            TipoSolicitud t = tipos.get(i);
            System.out.printf("  %d. %s (%d días estimados) - %s%n", i + 1, t.getNombre(), t.getTiempoEstimadoDias(), t.getDescripcion());
        }
        
        int idxTipo = leerOpcion(tipos.size(), "Seleccione el tipo de solicitud");
        if (idxTipo == -1) return;
        TipoSolicitud tipoSeleccionado = tipos.get(idxTipo);

        System.out.print("Descripción del caso (o '0' para cancelar): ");
        String descripcion = scanner.nextLine().trim();
        if (descripcion.equals("0") || descripcion.isEmpty()) {
            System.out.println("Operación cancelada.");
            return;
        }

        try {
            Solicitud solicitud = crearSolicitudUseCase.ejecutar(
                    new CrearSolicitudCommand(usuarioAutenticado.getId(), tipoSeleccionado.getId(), descripcion));
            System.out.println("Solicitud creada con éxito. ID asignado: " + solicitud.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registrarUsuario() {
        System.out.println("\n[ Registro de Usuario ]");
        
        String nombre = leerEntradaSimple("Nombre completo");
        if (nombre == null) return;
        if (!SOLO_LETRAS.matcher(nombre).matches()) {
            System.out.println("Error: El nombre contiene caracteres inválidos.");
            return;
        }
        
        String correo = leerEntradaSimple("Correo electrónico");
        if (correo == null) return;
        if (!CORREO_VALIDO.matcher(correo).matches()) {
            System.out.println("Error: Formato de correo no válido.");
            return;
        }
        
        System.out.println("Roles del sistema:");
        RolEnum[] roles = RolEnum.values();
        for (int i = 0; i < roles.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, roles[i]);
        }

        int idxRol = leerOpcion(roles.length, "Seleccione el rol");
        if (idxRol == -1) return;
        RolEnum rol = roles[idxRol];

        try {
            Usuario usuario = registrarUsuarioUseCase.ejecutar(new RegistrarUsuarioCommand(nombre, correo, rol));
            System.out.println("Usuario registrado. ID generado: " + usuario.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registrarTipoSolicitud() {
        System.out.println("\n[ Registro de Tipo de Solicitud ]");
        
        String nombre = leerEntradaSimple("Nombre del tipo de trámite");
        if (nombre == null) return;
        if (!NOMBRE_TIPO.matcher(nombre).matches()) {
            System.out.println("Error: Formato de nombre inválido.");
            return;
        }
        
        String descripcion = leerEntradaSimple("Descripción general");
        if (descripcion == null) return;
        
        System.out.print("Tiempo estimado de respuesta (en días): ");
        String diasStr = scanner.nextLine().trim();
        if (diasStr.equals("0")) return;
        
        try {
            int dias = Integer.parseInt(diasStr);
            if (dias <= 0) {
                System.out.println("Error: Los días deben ser mayores a cero.");
                return;
            }
            TipoSolicitud tipo = registrarTipoSolicitudUseCase.ejecutar(new RegistrarTipoSolicitudCommand(nombre, descripcion, dias));
            System.out.println("Tipo de solicitud guardado. ID: " + tipo.getId());
        } catch (NumberFormatException e) {
            System.out.println("Error: Debe ingresar un número entero.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cambiarEstado() {
        List<Solicitud> todas = listarTodasLasSolicitudesUseCase.ejecutar();
        if (todas.isEmpty()) {
            System.out.println("No existen solicitudes registradas.");
            return;
        }

        System.out.println("\nSolicitudes pendientes/activas:");
        for (int i = 0; i < todas.size(); i++) {
            Solicitud s = todas.get(i);
            System.out.printf("  %d. ID: %d [%s] - %s (Usuario: %s)%n", 
                    i + 1, s.getId(), s.getEstado(), s.getTipoSolicitud().getNombre(), s.getUsuario().getNombre());
        }

        int idx = leerOpcion(todas.size(), "Seleccione la solicitud a modificar");
        if (idx == -1) return;
        Solicitud seleccionada = todas.get(idx);

        System.out.println("\nEstados de transición:");
        EstadoEnum[] estados = EstadoEnum.values();
        for (int i = 0; i < estados.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, estados[i]);
        }

        int idxEstado = leerOpcion(estados.length, "Seleccione el nuevo estado");
        if (idxEstado == -1) return;
        EstadoEnum nuevoEstado = estados[idxEstado];

        try {
            cambiarEstadoSolicitudUseCase.ejecutar(new CambiarEstadoCommand(seleccionada.getId(), nuevoEstado));
            System.out.println("Estado actualizado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error de negocio: " + e.getMessage());
        }
    }

    private void consultarPorEstado() {
        System.out.println("\nFiltros de estado disponibles:");
        EstadoEnum[] estados = EstadoEnum.values();
        for (int i = 0; i < estados.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, estados[i]);
        }

        int idx = leerOpcion(estados.length, "Seleccione un filtro");
        if (idx == -1) return;
        EstadoEnum estadoBusqueda = estados[idx];

        List<Solicitud> filtradas = consultarSolicitudesPorEstadoUseCase.ejecutar(new ConsultarPorEstadoQuery(estadoBusqueda));
        
        if (usuarioAutenticado.getRol() == RolEnum.SOLICITANTE) {
            filtradas = filtradas.stream()
                    .filter(s -> s.getUsuario().getId().equals(usuarioAutenticado.getId()))
                    .toList();
        }

        System.out.println("\n--- RESULTADOS (" + estadoBusqueda + ") ---");
        if (filtradas.isEmpty()) {
            System.out.println("No se encontraron registros.");
            return;
        }

        for (Solicitud s : filtradas) {
            System.out.printf("ID: %d | Trámite: %s | Solicitante: %s | Creado: %s%n",
                    s.getId(), s.getTipoSolicitud().getNombre(), s.getUsuario().getNombre(), s.getFechaCreacion());
            System.out.println("Detalle: " + s.getDescripcion());
            System.out.println("----------------------------------------");
        }
    }

    private void generarReporte() {
        System.out.println("\n--- REPORTE GENERAL DEL SISTEMA ---");
        ReporteResponse reporte = generarReporteUseCase.ejecutar(new GenerarReporteQuery());
        System.out.println(reporte.toString());
    }

    private String leerEntradaSimple(String etiqueta) {
        System.out.print(etiqueta + " (o '0' para cancelar): ");
        String valor = scanner.nextLine().trim();
        if (valor.equals("0") || valor.isEmpty()) {
            System.out.println("Operación cancelada.");
            return null;
        }
        return valor;
    }

    private int leerOpcion(int max, String etiqueta) {
        while (true) {
            System.out.print(etiqueta + " (0 para volver): ");
            String entrada = scanner.nextLine().trim();
            if (entrada.equals("0")) return -1;
            try {
                int n = Integer.parseInt(entrada);
                if (n < 1 || n > max) {
                    System.out.println("Opción fuera de rango.");
                    continue;
                }
                return n - 1;
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero.");
            }
        }
    }
}