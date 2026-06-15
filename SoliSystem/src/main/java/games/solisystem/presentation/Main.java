package games.solisystem.presentation;

import games.solisystem.infrastructure.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Iniciando SoliSystem ===");
        
        // Inicializar la base de datos con el archivo schema.sql
        System.out.println("Inicializando base de datos...");
        DatabaseConfig.initializeDatabase();

        // Probar si la conexión se mantiene abierta y funciona correctamente
        try (Connection conn = DatabaseConfig.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("¡Conexión de prueba a PostgreSQL exitosa!");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }
}
