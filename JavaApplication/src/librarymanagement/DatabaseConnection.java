
package librarymanagement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private static final String DB_URL = "jdbc:sqlite:library.db";
    private Connection conn;

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Database connected.");
            createUsersTableIfNeeded();  // Ensure the 'users' table exists
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    // Singleton pattern to ensure only one connection is used
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Get the connection to the database
    public Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                // Re-establish connection if it's closed
                conn = DriverManager.getConnection(DB_URL);
                System.out.println("Re-established database connection.");
            }
        } catch (SQLException e) {
            System.err.println("Error re-establishing connection: " + e.getMessage());
        }
        return conn;
    }

    // Ensure users table exists
    private void createUsersTableIfNeeded() {
        try (Statement stmt = conn.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "role TEXT NOT NULL)";
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.err.println("Error creating users table: " + e.getMessage());
        }
    }

    // Close the connection when it's no longer needed
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
