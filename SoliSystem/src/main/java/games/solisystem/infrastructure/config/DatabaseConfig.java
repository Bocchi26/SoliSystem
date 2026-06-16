package games.solisystem.infrastructure.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456789";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de PostgreSQL en el classpath.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public static void initializeDatabase() {
        String schemaPath = "/games/solisystem/infrastructure/schema.sql";
        try (InputStream is = DatabaseConfig.class.getResourceAsStream(schemaPath)) {
            if (is == null) {
                System.err.println("No se pudo encontrar el archivo " + schemaPath + " en el classpath.");
                return;
            }

            String sql = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));

            try (Connection conn = getConnection();
                    Statement stmt = conn.createStatement()) {
                String[] statements = sql.split(";");
                for (String statement : statements) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
                System.out.println("Base de datos inicializada correctamente (schema.sql ejecutado).");
            }

        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
