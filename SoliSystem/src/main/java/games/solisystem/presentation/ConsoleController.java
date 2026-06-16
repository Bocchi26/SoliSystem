package games.solisystem.presentation;

import java.util.List;
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
import games.solisystem.domain.entity.Solicitud;
import games.solisystem.domain.entity.TipoSolicitud;
import games.solisystem.domain.entity.Usuario;
import games.solisystem.domain.enums.EstadoEnum;
import games.solisystem.domain.enums.RolEnum;
import games.solisystem.domain.repository.SolicitudRepository;
import games.solisystem.domain.repository.TipoSolicitudRepository;
import games.solisystem.domain.repository.UsuarioRepository;

public class ConsoleController {

    // ── Patrones de validación ────────────────────────────────────────────────
    /** Solo letras (incluyendo acentos y ñ) y espacios — sin números ni símbolos */
    private static final Pattern SOLO_LETRAS   = Pattern.compile("^[\\p{L} .'-]+$");
    /** Formato básico de correo electrónico */
    private static final Pattern CORREO_VALIDO = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    /** Descripción: al menos 10 caracteres, sin restricción de caracteres */
    private static final int MIN_DESCRIPCION   = 10;
    /** Nombre de tipo de solicitud: solo letras, números y espacios */
    private static final Pattern NOMBRE_TIPO   = Pattern.compile("^[\\p{L}\\p{N} .,-]+$");

    // ── Dependencias ──────────────────────────────────────────────────────────
    private final RegistrarUsuarioUseCase              registrarUsuarioUseCase;
    private final RegistrarTipoSolicitudUseCase        registrarTipoSolicitudUseCase;
    private final CrearSolicitudUseCase                crearSolicitudUseCase;
    private final CambiarEstadoSolicitudUseCase        cambiarEstadoSolicitudUseCase;
    private final ConsultarSolicitudesPorEstadoUseCase consultarPorEstadoUseCase;
    private final GenerarReporteUseCase                generarReporteUseCase;

    private final UsuarioRepository       usuarioRepository;
    private final TipoSolicitudRepository tipoSolicitudRepository;
    private final SolicitudRepository     solicitudRepository;

    private final Scanner scanner = new Scanner(System.in);

    public ConsoleController(
            RegistrarUsuarioUseCase registrarUsuarioUseCase,
            RegistrarTipoSolicitudUseCase registrarTipoSolicitudUseCase,
            CrearSolicitudUseCase crearSolicitudUseCase,
            CambiarEstadoSolicitudUseCase cambiarEstadoSolicitudUseCase,
            ConsultarSolicitudesPorEstadoUseCase consultarPorEstadoUseCase,
            GenerarReporteUseCase generarReporteUseCase,
            UsuarioRepository usuarioRepository,
            TipoSolicitudRepository tipoSolicitudRepository,
            SolicitudRepository solicitudRepository) {

        this.registrarUsuarioUseCase       = registrarUsuarioUseCase;
        this.registrarTipoSolicitudUseCase = registrarTipoSolicitudUseCase;
        this.crearSolicitudUseCase         = crearSolicitudUseCase;
        this.cambiarEstadoSolicitudUseCase = cambiarEstadoSolicitudUseCase;
        this.consultarPorEstadoUseCase     = consultarPorEstadoUseCase;
        this.generarReporteUseCase         = generarReporteUseCase;
        this.usuarioRepository             = usuarioRepository;
        this.tipoSolicitudRepository       = tipoSolicitudRepository;
        this.solicitudRepository           = solicitudRepository;
    }

    // ── Menú principal ────────────────────────────────────────────────────────
    public void iniciar() {
        linea();
        System.out.println("  Bienvenido a SolySistem");
        System.out.println("  Sistema de Gestión de Solicitudes Empresariales");
        linea();

        boolean activo = true;
        while (activo) {
            System.out.println("\n  ¿Qué desea hacer?");
            System.out.println("  1. Registrar nuevo usuario");
            System.out.println("  2. Registrar tipo de solicitud");
            System.out.println("  3. Crear una solicitud");
            System.out.println("  4. Cambiar estado de una solicitud");
            System.out.println("  5. Ver solicitudes por estado");
            System.out.println("  6. Generar reporte general");
            System.out.println("  0. Salir");
            System.out.print("\n  Opción: ");

            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1" -> registrarUsuario();
                case "2" -> registrarTipoSolicitud();
                case "3" -> crearSolicitud();
                case "4" -> cambiarEstado();
                case "5" -> consultarPorEstado();
                case "6" -> generarReporte();
                case "0" -> activo = false;
                default  -> error("Opción no válida. Elija entre 0 y 6.");
            }
        }
        System.out.println("\n  Hasta luego.\n");
    }

    // ── CU1: Registrar usuario ────────────────────────────────────────────────
    private void registrarUsuario() {
        titulo("Registrar Nuevo Usuario");
        aviso("Escriba 0 en cualquier campo para cancelar y regresar al menú.");

        // Nombre: solo letras y espacios, sin números
        String nombre = leerNombre("Nombre completo");
        if (nombre == null) { cancelado(); return; }

        // Correo: formato válido
        String correo = leerCorreo("Correo electrónico");
        if (correo == null) { cancelado(); return; }

        // Rol: menú de selección con reintento
        RolEnum rol = leerRol();
        if (rol == null) { cancelado(); return; }

        try {
            Usuario usuario = registrarUsuarioUseCase.ejecutar(
                    new RegistrarUsuarioCommand(nombre, correo, rol));
            exito("Usuario registrado correctamente. ID asignado: " + usuario.getId());
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
        }
    }

    // ── CU2: Registrar tipo de solicitud ──────────────────────────────────────
    private void registrarTipoSolicitud() {
        titulo("Registrar Tipo de Solicitud");
        aviso("Escriba 0 en cualquier campo para cancelar y regresar al menú.");

        // Nombre del tipo: solo letras, números y espacios
        String nombre = leerNombreTipo("Nombre del tipo");
        if (nombre == null) { cancelado(); return; }

        // Descripción: texto libre, mínimo 10 caracteres
        String descripcion = leerDescripcion("Descripción");
        if (descripcion == null) { cancelado(); return; }

        // Días: número entero positivo
        Integer dias = leerEnteroPositivo("Tiempo estimado de resolución (en días)");
        if (dias == null) { cancelado(); return; }

        try {
            TipoSolicitud tipo = registrarTipoSolicitudUseCase.ejecutar(
                    new RegistrarTipoSolicitudCommand(nombre, descripcion, dias));
            exito("Tipo de solicitud registrado. ID asignado: " + tipo.getId());
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
        }
    }

    // ── CU3: Crear solicitud ──────────────────────────────────────────────────
    private void crearSolicitud() {
        titulo("Crear Nueva Solicitud");
        aviso("Escriba 0 en cualquier selección para cancelar y regresar al menú.");

        // Usuarios solicitantes disponibles
        List<Usuario> solicitantes = usuarioRepository.buscarTodos().stream()
                .filter(u -> u.getRol() == RolEnum.SOLICITANTE)
                .toList();

        if (solicitantes.isEmpty()) {
            error("No hay usuarios con rol SOLICITANTE registrados. Registre uno primero.");
            return;
        }

        System.out.println("  Usuarios disponibles:");
        for (int i = 0; i < solicitantes.size(); i++) {
            Usuario u = solicitantes.get(i);
            System.out.printf("  %d. %s (%s)%n", i + 1, u.getNombre(), u.getCorreo());
        }

        int idxUsuario = leerOpcion(solicitantes.size(), "Seleccione el usuario (número)");
        if (idxUsuario == -1) { cancelado(); return; }
        Usuario usuarioSeleccionado = solicitantes.get(idxUsuario);

        // Tipos de solicitud disponibles
        List<TipoSolicitud> tipos = tipoSolicitudRepository.buscarTodos();
        if (tipos.isEmpty()) {
            error("No hay tipos de solicitud registrados. Registre uno primero.");
            return;
        }

        System.out.println("\n  Tipos de solicitud disponibles:");
        for (int i = 0; i < tipos.size(); i++) {
            TipoSolicitud t = tipos.get(i);
            System.out.printf("  %d. %s — %s (estimado: %d días)%n",
                    i + 1, t.getNombre(), t.getDescripcion(), t.getTiempoEstimadoDias());
        }

        int idxTipo = leerOpcion(tipos.size(), "Seleccione el tipo (número)");
        if (idxTipo == -1) { cancelado(); return; }
        TipoSolicitud tipoSeleccionado = tipos.get(idxTipo);

        // Descripción detallada: mínimo 10 caracteres
        String descripcion = leerDescripcion("Descripción detallada de la solicitud");
        if (descripcion == null) { cancelado(); return; }

        try {
            Solicitud solicitud = crearSolicitudUseCase.ejecutar(
                    new CrearSolicitudCommand(
                            usuarioSeleccionado.getId(),
                            tipoSeleccionado.getId(),
                            descripcion));
            exito("Solicitud creada. ID: " + solicitud.getId()
                    + " | Estado inicial: " + solicitud.getEstado());
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
        }
    }

    // ── CU4: Cambiar estado ───────────────────────────────────────────────────
    private void cambiarEstado() {
        titulo("Cambiar Estado de una Solicitud");
        aviso("Escriba 0 en cualquier selección para cancelar y regresar al menú.");

        List<Solicitud> todas = solicitudRepository.buscarTodos();
        if (todas.isEmpty()) {
            error("No hay solicitudes registradas en el sistema.");
            return;
        }

        System.out.println("  Solicitudes activas:");
        for (int i = 0; i < todas.size(); i++) {
            Solicitud s = todas.get(i);
            String desc = s.getDescripcion().length() > 40
                    ? s.getDescripcion().substring(0, 40) + "..."
                    : s.getDescripcion();
            System.out.printf("  %d. [%s] ID:%d — %s (Usuario: %s)%n",
                    i + 1, s.getEstado(), s.getId(), desc, s.getUsuario().getNombre());
        }

        int idx = leerOpcion(todas.size(), "Seleccione la solicitud a actualizar (número)");
        if (idx == -1) { cancelado(); return; }
        Solicitud seleccionada = todas.get(idx);

        List<EstadoEnum> siguientes = transicionesPosibles(seleccionada.getEstado());
        if (siguientes.isEmpty()) {
            aviso("Esta solicitud ya está en estado final (CERRADA). No se puede cambiar.");
            return;
        }

        System.out.println("\n  Estado actual: " + seleccionada.getEstado());
        System.out.println("  Puede cambiar a:");
        for (int i = 0; i < siguientes.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, siguientes.get(i));
        }

        int idxEstado = leerOpcion(siguientes.size(), "Seleccione el nuevo estado (número)");
        if (idxEstado == -1) { cancelado(); return; }
        EstadoEnum nuevoEstado = siguientes.get(idxEstado);

        try {
            Solicitud actualizada = cambiarEstadoSolicitudUseCase.ejecutar(
                    new CambiarEstadoCommand(seleccionada.getId(), nuevoEstado));
            exito("Estado actualizado a: " + actualizada.getEstado()
                    + " | Notificación registrada automáticamente.");
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
        }
    }

    // ── CU5: Consultar por estado ─────────────────────────────────────────────
    private void consultarPorEstado() {
        titulo("Consultar Solicitudes por Estado");
        aviso("Escriba 0 para cancelar y regresar al menú.");

        EstadoEnum[] estados = EstadoEnum.values();
        System.out.println("  Seleccione el estado a consultar:");
        for (int i = 0; i < estados.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, estados[i]);
        }

        int idx = leerOpcion(estados.length, "Opción");
        if (idx == -1) { cancelado(); return; }
        EstadoEnum estado = estados[idx];

        List<Solicitud> resultados = consultarPorEstadoUseCase.ejecutar(
                new ConsultarPorEstadoQuery(estado));

        if (resultados.isEmpty()) {
            System.out.println("  No hay solicitudes con estado " + estado + ".");
            return;
        }

        System.out.println("\n  Solicitudes en estado " + estado
                + " (" + resultados.size() + " encontradas):");
        linea();
        for (Solicitud s : resultados) {
            System.out.printf("  ID: %d%n",         s.getId());
            System.out.printf("  Usuario:  %s%n",   s.getUsuario().getNombre());
            System.out.printf("  Tipo:     %s%n",   s.getTipoSolicitud().getNombre());
            System.out.printf("  Fecha:    %s%n",   s.getFechaCreacion());
            System.out.printf("  Detalle:  %s%n",   s.getDescripcion());
            linea();
        }
    }

    // ── CU6: Generar reporte ──────────────────────────────────────────────────
    private void generarReporte() {
        titulo("Reporte General de Solicitudes");
        try {
            ReporteResponse r = generarReporteUseCase.ejecutar(new GenerarReporteQuery());
            System.out.printf("  Total de solicitudes : %d%n", r.getTotal());
            linea();
            System.out.printf("  CREADAS              : %d%n", r.getCreadas());
            System.out.printf("  EN REVISIÓN          : %d%n", r.getEnRevision());
            System.out.printf("  APROBADAS            : %d%n", r.getAprobadas());
            System.out.printf("  RECHAZADAS           : %d%n", r.getRechazadas());
            System.out.printf("  CERRADAS             : %d%n", r.getCerradas());
            linea();
        } catch (Exception e) {
            error("No se pudo generar el reporte: " + e.getMessage());
        }
    }

    // =========================================================================
    // ── Métodos de lectura con validación y reintento ─────────────────────────
    // =========================================================================

    /**
     * Lee un nombre de persona: solo letras (con acentos y ñ), espacios, puntos,
     * apóstrofes y guiones. No permite números ni caracteres especiales.
     * Repite hasta recibir un valor válido o que el usuario escriba "0".
     *
     * @return el nombre válido, o null si el usuario canceló escribiendo "0".
     */
    private String leerNombre(String etiqueta) {
        while (true) {
            System.out.print("  " + etiqueta + ": ");
            String valor = scanner.nextLine().trim();

            if (valor.equals("0")) return null;

            if (valor.isEmpty()) {
                error("El campo no puede estar vacío. Intente de nuevo.");
                continue;
            }
            if (!SOLO_LETRAS.matcher(valor).matches()) {
                error("El nombre solo puede contener letras y espacios (sin números ni símbolos). Intente de nuevo.");
                continue;
            }
            if (valor.length() < 2) {
                error("El nombre debe tener al menos 2 caracteres. Intente de nuevo.");
                continue;
            }
            return valor;
        }
    }

    /**
     * Lee un nombre de tipo de solicitud: letras, números, espacios y puntuación básica.
     * No puede estar vacío ni ser demasiado corto.
     *
     * @return el nombre válido, o null si el usuario canceló.
     */
    private String leerNombreTipo(String etiqueta) {
        while (true) {
            System.out.print("  " + etiqueta + ": ");
            String valor = scanner.nextLine().trim();

            if (valor.equals("0")) return null;

            if (valor.isEmpty()) {
                error("El campo no puede estar vacío. Intente de nuevo.");
                continue;
            }
            if (!NOMBRE_TIPO.matcher(valor).matches()) {
                error("El nombre solo puede contener letras, números, espacios y puntuación básica (., -). Intente de nuevo.");
                continue;
            }
            if (valor.length() < 3) {
                error("El nombre debe tener al menos 3 caracteres. Intente de nuevo.");
                continue;
            }
            return valor;
        }
    }

    /**
     * Lee un correo electrónico con formato válido.
     * Repite hasta recibir un formato correcto o cancelar con "0".
     *
     * @return el correo válido, o null si canceló.
     */
    private String leerCorreo(String etiqueta) {
        while (true) {
            System.out.print("  " + etiqueta + ": ");
            String valor = scanner.nextLine().trim();

            if (valor.equals("0")) return null;

            if (valor.isEmpty()) {
                error("El correo no puede estar vacío. Intente de nuevo.");
                continue;
            }
            if (!CORREO_VALIDO.matcher(valor).matches()) {
                error("Formato de correo inválido. Ejemplo correcto: usuario@dominio.com. Intente de nuevo.");
                continue;
            }
            return valor;
        }
    }

    /**
     * Lee una descripción de texto libre con longitud mínima.
     * Repite hasta recibir un texto suficientemente largo o cancelar con "0".
     *
     * @return la descripción válida, o null si canceló.
     */
    private String leerDescripcion(String etiqueta) {
        while (true) {
            System.out.print("  " + etiqueta + ": ");
            String valor = scanner.nextLine().trim();

            if (valor.equals("0")) return null;

            if (valor.isEmpty()) {
                error("La descripción no puede estar vacía. Intente de nuevo.");
                continue;
            }
            if (valor.length() < MIN_DESCRIPCION) {
                error("La descripción debe tener al menos " + MIN_DESCRIPCION
                        + " caracteres (tiene " + valor.length() + "). Intente de nuevo.");
                continue;
            }
            return valor;
        }
    }

    /**
     * Lee un número entero mayor que cero.
     * Repite hasta recibir un entero positivo válido o cancelar con "0".
     *
     * @return el entero positivo, o null si canceló.
     */
    private Integer leerEnteroPositivo(String etiqueta) {
        while (true) {
            System.out.print("  " + etiqueta + ": ");
            String entrada = scanner.nextLine().trim();

            if (entrada.equals("0")) return null;

            if (entrada.isEmpty()) {
                error("El campo no puede estar vacío. Ingrese un número entero positivo.");
                continue;
            }

            // Verificar que no contenga letras antes de parsear
            if (!entrada.matches("\\d+")) {
                error("Debe ingresar únicamente dígitos (número entero positivo). Intente de nuevo.");
                continue;
            }

            try {
                int valor = Integer.parseInt(entrada);
                if (valor <= 0) {
                    error("El número debe ser mayor que cero. Intente de nuevo.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                error("Número demasiado grande. Ingrese un valor entero válido.");
            }
        }
    }

    /**
     * Lee la selección del rol (SOLICITANTE / FUNCIONARIO).
     * Repite hasta recibir una opción válida o cancelar con "0".
     *
     * @return el RolEnum elegido, o null si canceló.
     */
    private RolEnum leerRol() {
        while (true) {
            System.out.println("  Tipo de usuario:");
            System.out.println("  1. SOLICITANTE  (puede crear solicitudes)");
            System.out.println("  2. FUNCIONARIO  (puede gestionar solicitudes)");
            System.out.println("  0. Cancelar");
            System.out.print("  Elija 0, 1 o 2: ");
            String rolOpc = scanner.nextLine().trim();

            switch (rolOpc) {
                case "0" -> { return null; }
                case "1" -> { return RolEnum.SOLICITANTE; }
                case "2" -> { return RolEnum.FUNCIONARIO; }
                default  -> error("Opción no válida. Ingrese 1, 2 o 0 para cancelar.");
            }
        }
    }

    /**
     * Lee una opción numérica entre 1 y max, con reintento automático.
     * El usuario puede escribir "0" para cancelar.
     *
     * @param max     número máximo de opción válida.
     * @param etiqueta texto del prompt mostrado al usuario.
     * @return el índice base-0 de la opción elegida, o -1 si canceló con "0".
     */
    private int leerOpcion(int max, String etiqueta) {
        while (true) {
            System.out.print("  " + etiqueta + " (0 para cancelar): ");
            String entrada = scanner.nextLine().trim();

            if (entrada.equals("0")) return -1;

            if (entrada.isEmpty()) {
                error("Debe ingresar un número. Intente de nuevo.");
                continue;
            }

            if (!entrada.matches("\\d+")) {
                error("Solo se aceptan números. Intente de nuevo.");
                continue;
            }

            try {
                int n = Integer.parseInt(entrada);
                if (n < 1 || n > max) {
                    error("Ingrese un número entre 1 y " + max + ". Intente de nuevo.");
                    continue;
                }
                return n - 1;
            } catch (NumberFormatException e) {
                error("Número inválido. Intente de nuevo.");
            }
        }
    }

    // ── Helpers de dominio ────────────────────────────────────────────────────

    /** Devuelve los estados a los que se puede transicionar desde el estado actual */
    private List<EstadoEnum> transicionesPosibles(EstadoEnum actual) {
        return switch (actual) {
            case CREADA      -> List.of(EstadoEnum.EN_REVISION);
            case EN_REVISION -> List.of(EstadoEnum.APROBADA, EstadoEnum.RECHAZADA);
            case APROBADA,
                 RECHAZADA   -> List.of(EstadoEnum.CERRADA);
            case CERRADA     -> List.of();
        };
    }

    // ── Helpers de UI ─────────────────────────────────────────────────────────

    private void titulo(String texto) {
        System.out.println();
        linea();
        System.out.println("  " + texto);
        linea();
    }

    private void linea() {
        System.out.println("  ----------------------------------------");
    }

    private void exito(String mensaje) {
        System.out.println("  ✔ " + mensaje);
    }

    private void error(String mensaje) {
        System.out.println("  ✘ Error: " + mensaje);
    }

    private void aviso(String mensaje) {
        System.out.println("  ℹ  " + mensaje);
    }

    private void cancelado() {
        System.out.println("  ↩  Operación cancelada. Regresando al menú principal.");
    }
}