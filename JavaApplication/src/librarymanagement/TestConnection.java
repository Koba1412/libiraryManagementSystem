
package librarymanagement;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    public void testConnection() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            if (conn != null) {
                System.out.println("Database connection successful!");
            } else {
                System.out.println("Database connection failed!");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Create an instance of TestConnection and call testConnection method
        TestConnection testConn = new TestConnection();
        testConn.testConnection();
    }
}
