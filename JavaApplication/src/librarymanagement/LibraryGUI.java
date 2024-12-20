package librarymanagement;
import javax.swing.*;  
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryGUI extends JFrame {
    private JTextArea logArea;
    private JTable bookTable, userTable, transactionTable;
    private DefaultTableModel bookTableModel, userTableModel, transactionTableModel;
    private Logger logger;
    private List<Book> books;

    public LibraryGUI() {
        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        logger = Logger.getInstance();
        books = new ArrayList<>();

        // Log Area
        logArea = new JTextArea(5, 20);
        logArea.setEditable(false);
        logger.addObserver(new LogAreaObserver(logArea));
        add(new JScrollPane(logArea), BorderLayout.SOUTH);

        // Top Panel with Buttons
        JPanel topPanel = new JPanel();
        JButton manageBooksButton = new JButton("Manage Books");
        JButton manageUsersButton = new JButton("Manage Users");
        JButton viewTransactionsButton = new JButton("View Transactions");
        topPanel.add(manageBooksButton);
        topPanel.add(manageUsersButton);
        topPanel.add(viewTransactionsButton);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel with Tables
        JPanel centerPanel = new JPanel(new CardLayout());

        // Book Management Panel
        JPanel bookPanel = new JPanel(new BorderLayout());
        bookTableModel = new DefaultTableModel(new String[]{"Title", "Type", "Status"}, 0);
        bookTable = new JTable(bookTableModel);
        bookPanel.add(new JScrollPane(bookTable), BorderLayout.CENTER);

        JPanel bookInputPanel = new JPanel();
        JTextField bookTitleField = new JTextField(10);
        JComboBox<String> bookTypeCombo = new JComboBox<>(new String[]{"Software Engineering", "Management", "Artificial Intelligence"});
        JButton addBookButton = new JButton("Add Book");
        JButton borrowBookButton = new JButton("Borrow Book");
        JButton returnBookButton = new JButton("Return Book");
        bookInputPanel.add(new JLabel("Title:"));
        bookInputPanel.add(bookTitleField);
        bookInputPanel.add(new JLabel("Type:"));
        bookInputPanel.add(bookTypeCombo);
        bookInputPanel.add(addBookButton);
        bookInputPanel.add(borrowBookButton);
        bookInputPanel.add(returnBookButton);
        bookPanel.add(bookInputPanel, BorderLayout.SOUTH);

        centerPanel.add(bookPanel, "Manage Books");

        // User Management Panel
        JPanel userPanel = new JPanel(new BorderLayout());
        userTableModel = new DefaultTableModel(new String[]{"Name", "Role"}, 0);
        userTable = new JTable(userTableModel);
        userPanel.add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel userInputPanel = new JPanel();
        JTextField userNameField = new JTextField(10);
        JComboBox<String> userRoleCombo = new JComboBox<>(new String[]{"Admin", "Regular"});
        JButton addUserButton = new JButton("Add User");
        userInputPanel.add(new JLabel("Name:"));
        userInputPanel.add(userNameField);
        userInputPanel.add(new JLabel("Role:"));
        userInputPanel.add(userRoleCombo);
        userInputPanel.add(addUserButton);
        userPanel.add(userInputPanel, BorderLayout.SOUTH);

        centerPanel.add(userPanel, "Manage Users");

        // Transaction Panel
        JPanel transactionPanel = new JPanel(new BorderLayout());
        transactionTableModel = new DefaultTableModel(new String[]{"User", "Book", "Action", "Date"}, 0);
        transactionTable = new JTable(transactionTableModel);
        transactionPanel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        centerPanel.add(transactionPanel, "View Transactions");

        add(centerPanel, BorderLayout.CENTER);

        // Action Listeners
        manageBooksButton.addActionListener(e -> {
            ((CardLayout) centerPanel.getLayout()).show(centerPanel, "Manage Books");
        });

        manageUsersButton.addActionListener(e -> {
            ((CardLayout) centerPanel.getLayout()).show(centerPanel, "Manage Users");
        });

        viewTransactionsButton.addActionListener(e -> {
            ((CardLayout) centerPanel.getLayout()).show(centerPanel, "View Transactions");
        });

        // Add Book Button Action Listener
        addBookButton.addActionListener(e -> {
            String title = bookTitleField.getText();
            String type = (String) bookTypeCombo.getSelectedItem();
            if (title.isEmpty() || type == null) {
                logger.log("Error: Book title or type is missing.");
                return;
            }

            Book book = BookFactory.createBook(title, type);
            books.add(book);
            bookTableModel.addRow(new Object[]{book.getTitle(), book.getType(), "Available"});
            logger.log("Added book: " + book.getDescription());

            // Database interaction to insert book
            try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO books (title, type, status) VALUES (?, ?, ?)");
                stmt.setString(1, book.getTitle());
                stmt.setString(2, book.getType());
                stmt.setString(3, "Available");
                stmt.executeUpdate();
            } catch (SQLException ex) {
                logger.log("Database error: " + ex.getMessage());
            }

            bookTitleField.setText("");
        });

        // Add User Button Action Listener
addUserButton.addActionListener(e -> {
    String userName = userNameField.getText();
    String role = (String) userRoleCombo.getSelectedItem();
    if (userName.isEmpty() || role == null) {
        logger.log("Error: User name or role is missing.");
        return;
    }

    // Check if connection is valid
    Connection conn = DatabaseConnection.getInstance().getConnection();
    if (conn == null) {
        logger.log("Error: Failed to establish a database connection.");
        return;
    }

    // Add user to the database
    try {
        String sql = "INSERT INTO users (name, role) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, userName);
        stmt.setString(2, role);
        stmt.executeUpdate();

        userTableModel.addRow(new Object[]{userName, role});
        logger.log("Added user: " + userName + " with role: " + role);

        userNameField.setText("");
    } catch (SQLException ex) {
        logger.log("Database error: " + ex.getMessage());
    }
});

        // Borrow Book Action Listener
        borrowBookButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow == -1) {
                logger.log("Error: No book selected to borrow.");
                return;
            }

            String title = (String) bookTableModel.getValueAt(selectedRow, 0);
            String status = (String) bookTableModel.getValueAt(selectedRow, 2);

            if ("Borrowed".equalsIgnoreCase(status)) {
                logger.log("Error: Book is already borrowed.");
                return;
            }

            bookTableModel.setValueAt("Borrowed", selectedRow, 2);
            String user = JOptionPane.showInputDialog("Enter user name for borrowing:");

            if (user != null && !user.isEmpty()) {
                LocalDate date = LocalDate.now();
                transactionTableModel.addRow(new Object[]{user, title, "Borrowed", date.toString()});
                logger.log(user + " borrowed the book: " + title);

                // Database interaction to record the borrowing transaction
                try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO transactions (user, book, action, date) VALUES (?, ?, ?, ?)");
                    stmt.setString(1, user);
                    stmt.setString(2, title);
                    stmt.setString(3, "Borrowed");
                    stmt.setString(4, date.toString());
                    stmt.executeUpdate();

                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE books SET status = ? WHERE title = ?");
                    updateStmt.setString(1, "Borrowed");
                    updateStmt.setString(2, title);
                    updateStmt.executeUpdate();
                } catch (SQLException ex) {
                    logger.log("Database error: " + ex.getMessage());
                }
            }
        });

        // Return Book Action Listener
        returnBookButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow == -1) {
                logger.log("Error: No book selected to return.");
                return;
            }

            String title = (String) bookTableModel.getValueAt(selectedRow, 0);
            String status = (String) bookTableModel.getValueAt(selectedRow, 2);

            if (!"Borrowed".equalsIgnoreCase(status)) {
                logger.log("Error: Book is not currently borrowed.");
                return;
            }

            bookTableModel.setValueAt("Available", selectedRow, 2);
            String user = JOptionPane.showInputDialog("Enter user name for returning:");

            if (user != null && !user.isEmpty()) {
                LocalDate date = LocalDate.now();
                transactionTableModel.addRow(new Object[]{user, title, "Returned", date.toString()});
                logger.log(user + " returned the book: " + title);

                // Database interaction to record the return transaction
                try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO transactions (user, book, action, date) VALUES (?, ?, ?, ?)");
                    stmt.setString(1, user);
                    stmt.setString(2, title);
                    stmt.setString(3, "Returned");
                    stmt.setString(4, date.toString());
                    stmt.executeUpdate();

                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE books SET status = ? WHERE title = ?");
                    updateStmt.setString(1, "Available");
                    updateStmt.setString(2, title);
                    updateStmt.executeUpdate();
                } catch (SQLException ex) {
                    logger.log("Database error: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryGUI libraryGUI = new LibraryGUI();
            libraryGUI.setVisible(true);
        });
    }
}

