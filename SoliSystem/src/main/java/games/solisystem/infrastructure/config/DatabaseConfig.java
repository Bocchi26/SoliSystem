package games.solisystem.infrastructure.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    // Configura aquí las credenciales reales de tu base de datos PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/solisystem";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}