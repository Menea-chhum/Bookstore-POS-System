package form;

import database.DBConnection;
import model.staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Date;

/**
 * NewSale Class - Handles the Point of Sale operations
 */
public class NewSale {
    // Root Panel - MUST match exactly what's in the .form file
    private JPanel MainPanel;  // Capital M - matches the .form file

    // Panel Components
    private JPanel HeaderPanel;
    private JPanel CenterPanel;
    private JPanel BookPanel;
    private JPanel CartPanel;
    private JPanel ButtomPanel;
    private JPanel usernamePanel;

    // Additional Panels
    private JPanel header;
    private JPanel left;
    private JPanel right;
    private JPanel bottom;

    // Labels
    private JLabel logoLabel;
    private JLabel storeNameLabel;
    private JLabel Username;
    private JLabel accountLabel;

    // Buttons
    private JButton LogoutBtn;
    private JButton searchBtn;
    private JButton button1;
    private JButton button2;
    private JButton Confirm;
    private JButton cancelBtn;

    // Input Components
    private JTextField textField1;
    private JComboBox<String> comboBox1;
    private JTable table1;
    private JTable table2;
    private JSpinner spinner1;

    // Table Models
    private DefaultTableModel productModel;
    private DefaultTableModel cartModel;

    // Current logged-in staff
    private staff currentStaff;

    // Flag to prevent search on initialization
    private boolean isInitializing = true;

    // Labels for total display
    private JLabel totalBooksLabel;
    private JLabel totalAmountLabel;

    /**
     * Constructor for NewSale
     */
    public NewSale() {
        this(null);
    }

    /**
     * Constructor with staff parameter
     * @param staff Logged-in staff member
     */
    public NewSale(staff staff) {
        this.currentStaff = staff;

        System.out.println("NewSale constructor received staff: " + (staff != null ? staff.getFull_name() : "null"));
        if (staff != null) {
            System.out.println("Staff ID: " + staff.getStaffId());
            System.out.println("Staff Role: " + staff.getRole());
        }

        initializeTables();
        findTotalLabels();
        setupListeners();
        loadCategories();
        loadBooks();
        setupSpinner();
        displayUsername();
        isInitializing = false;
    }

    /**
     * Get the main panel for this form
     * @return MainPanel
     */
    public JPanel getMainPanel() {
        return MainPanel;
    }

    /**
     * Set the current logged-in staff
     * @param staff Staff object
     */
    public void setCurrentStaff(staff staff) {
        this.currentStaff = staff;
        displayUsername();
    }

    /**
     * Display username in the header
     */
    private void displayUsername() {
        if (currentStaff != null && Username != null) {
            Username.setText(currentStaff.getFull_name());
        }
        if (currentStaff != null && accountLabel != null) {
            accountLabel.setText(currentStaff.getRole());
        }
    }

    /**
     * Find total labels in the form
     */
    private void findTotalLabels() {
        // Search for labels in CartPanel
        if (CartPanel != null) {
            findLabelsInContainer(CartPanel);
        }
    }

    /**
     * Recursively find labels in container
     */
    private void findLabelsInContainer(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                String text = label.getText();
                if (text != null) {
                    if (text.contains("Total Books")) {
                        totalBooksLabel = label;
                    } else if (text.contains("Total Amount")) {
                        totalAmountLabel = label;
                    }
                }
            } else if (comp instanceof Container) {
                findLabelsInContainer((Container) comp);
            }
        }
    }

    /**
     * Initialize table models and configure tables
     */
    private void initializeTables() {
        // Book List Table (table1)
        String[] productColumns = {"Book ID", "Title", "Author", "Price", "Stock"};
        productModel = new DefaultTableModel(productColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table1.setModel(productModel);

        // Shopping Cart Table (table2)
        String[] cartColumns = {"Book ID", "Title", "Price", "Quantity", "Subtotal"};
        cartModel = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table2.setModel(cartModel);

        // Add listener to update totals when cart changes
        cartModel.addTableModelListener(e -> updateTotalDisplay());
    }

    /**
     * Setup the quantity spinner with default value and range
     */
    private void setupSpinner() {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 999, 1);
        spinner1.setModel(spinnerModel);
    }

    /**
     * Setup action listeners for all buttons
     */
    private void setupListeners() {
        // Search button listener
        if (searchBtn != null) {
            searchBtn.addActionListener(e -> {
                if (!isInitializing) {
                    searchBooks();
                }
            });
        }

        // Enter key press on search field
        if (textField1 != null) {
            textField1.addActionListener(e -> {
                if (!isInitializing) {
                    searchBooks();
                }
            });
        }

        // Category filter combo box listener
        if (comboBox1 != null) {
            comboBox1.addActionListener(e -> {
                if (!isInitializing) {
                    filterByCategory();
                }
            });
        }

        // Add To Cart button
        if (button1 != null) {
            button1.addActionListener(e -> addToCart());
        }

        // Remove Selected button
        if (button2 != null) {
            button2.addActionListener(e -> removeFromCart());
        }

        // Confirm Sale button
        if (Confirm != null) {
            Confirm.addActionListener(e -> confirmSale());
        }

        // Cancel button
        if (cancelBtn != null) {
            cancelBtn.addActionListener(e -> cancelSale());
        }

        // Logout button
        if (LogoutBtn != null) {
            LogoutBtn.addActionListener(e -> logout());
        }

        // Double click on book table to add to cart
        if (table1 != null) {
            table1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        addToCart();
                    }
                }
            });
        }
    }

    /**
     * Load all categories from database into comboBox1
     */
    private void loadCategories() {
        if (comboBox1 == null) return;

        comboBox1.removeAllItems();
        comboBox1.addItem("All Categories");

        String query = "SELECT category_name FROM category ORDER BY category_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                comboBox1.addItem(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainPanel,
                    "Error loading categories: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load all books from database into table1
     */
    private void loadBooks() {
        String query = "SELECT b.book_id, b.title, b.author, b.price, b.quantity " +
                "FROM book b " +
                "ORDER BY b.title";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            productModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                };
                productModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainPanel,
                    "Error loading books: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Search books by ID or Title
     */
    private void searchBooks() {
        String searchText = textField1.getText().trim();
        String selectedCategory = (String) comboBox1.getSelectedItem();

        // If search text is empty and category is "All Categories", just load all books
        if (searchText.isEmpty() && (selectedCategory == null || selectedCategory.equals("All Categories"))) {
            loadBooks();
            return;
        }

        StringBuilder query = new StringBuilder(
                "SELECT b.book_id, b.title, b.author, b.price, b.quantity " +
                        "FROM book b " +
                        "LEFT JOIN category c ON b.category_id = c.category_id " +
                        "WHERE 1=1 "
        );

        // Add search condition
        if (!searchText.isEmpty()) {
            // Check if search text is a number (book ID)
            try {
                Integer.parseInt(searchText);
                query.append("AND b.book_id = ? ");
            } catch (NumberFormatException e) {
                query.append("AND b.title LIKE ? ");
            }
        }

        // Add category filter
        if (selectedCategory != null && !selectedCategory.equals("All Categories")) {
            query.append("AND c.category_name = ? ");
        }

        query.append("ORDER BY b.title");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query.toString())) {

            int paramIndex = 1;

            // Set search parameters
            if (!searchText.isEmpty()) {
                try {
                    int bookId = Integer.parseInt(searchText);
                    pstmt.setInt(paramIndex++, bookId);
                } catch (NumberFormatException e) {
                    pstmt.setString(paramIndex++, "%" + searchText + "%");
                }
            }

            // Set category parameter
            if (selectedCategory != null && !selectedCategory.equals("All Categories")) {
                pstmt.setString(paramIndex++, selectedCategory);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                productModel.setRowCount(0);

                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    };
                    productModel.addRow(row);
                }

                if (productModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(MainPanel,
                            "No books found matching your search criteria.",
                            "Search Results",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainPanel,
                    "Error searching books: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Filter books by selected category
     */
    private void filterByCategory() {
        String selectedCategory = (String) comboBox1.getSelectedItem();
        String searchText = textField1.getText().trim();

        // If search text is not empty, use searchBooks which handles both
        if (!searchText.isEmpty()) {
            searchBooks();
            return;
        }

        // If "All Categories" is selected, load all books
        if (selectedCategory == null || selectedCategory.equals("All Categories")) {
            loadBooks();
            return;
        }

        String query = "SELECT b.book_id, b.title, b.author, b.price, b.quantity " +
                "FROM book b " +
                "LEFT JOIN category c ON b.category_id = c.category_id " +
                "WHERE c.category_name = ? " +
                "ORDER BY b.title";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, selectedCategory);

            try (ResultSet rs = pstmt.executeQuery()) {
                productModel.setRowCount(0);

                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    };
                    productModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainPanel,
                    "Error filtering books: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Add selected book to shopping cart
     */
    private void addToCart() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(MainPanel,
                    "Please select a book from the list.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get book details from table1
        int bookId = (int) table1.getValueAt(selectedRow, 0);
        String title = (String) table1.getValueAt(selectedRow, 1);
        double price = (double) table1.getValueAt(selectedRow, 3);
        int stock = (int) table1.getValueAt(selectedRow, 4);

        // Get quantity from spinner
        int quantity = (int) spinner1.getValue();

        // Check stock availability
        if (quantity > stock) {
            JOptionPane.showMessageDialog(MainPanel,
                    "Insufficient stock! Available: " + stock,
                    "Stock Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if book already exists in cart
        boolean found = false;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            if ((int) cartModel.getValueAt(i, 0) == bookId) {
                // Update existing cart item
                int currentQty = (int) cartModel.getValueAt(i, 3);
                int newQty = currentQty + quantity;

                if (newQty > stock) {
                    JOptionPane.showMessageDialog(MainPanel,
                            "Total quantity exceeds available stock!",
                            "Stock Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double subtotal = price * newQty;
                cartModel.setValueAt(newQty, i, 3);
                cartModel.setValueAt(subtotal, i, 4);
                found = true;
                break;
            }
        }

        if (!found) {
            // Add new item to cart
            double subtotal = price * quantity;
            Object[] row = {bookId, title, price, quantity, subtotal};
            cartModel.addRow(row);
        }

        // Update total display
        updateTotalDisplay();

        // Clear selection
        table1.clearSelection();

        // Reset spinner to 1
        spinner1.setValue(1);
    }

    /**
     * Remove selected item from shopping cart
     */
    private void removeFromCart() {
        int selectedRow = table2.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(MainPanel,
                    "Please select an item to remove from the cart.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(MainPanel,
                "Remove selected item from cart?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            cartModel.removeRow(selectedRow);
            updateTotalDisplay();
        }
    }

    /**
     * Update total display in labels
     */
    private void updateTotalDisplay() {
        double total = 0.0;
        int totalBooks = 0;

        for (int i = 0; i < cartModel.getRowCount(); i++) {
            total += (double) cartModel.getValueAt(i, 4);
            totalBooks += (int) cartModel.getValueAt(i, 3);
        }

        // Update the labels if found
        if (totalBooksLabel != null) {
            totalBooksLabel.setText("Total Books: " + totalBooks);
        }

        if (totalAmountLabel != null) {
            totalAmountLabel.setText(String.format("Total Amount: $%.2f", total));
        }

        // Fallback: Search for labels again if not found
        if (totalBooksLabel == null || totalAmountLabel == null) {
            findTotalLabels();
            if (totalBooksLabel != null) {
                totalBooksLabel.setText("Total Books: " + totalBooks);
            }
            if (totalAmountLabel != null) {
                totalAmountLabel.setText(String.format("Total Amount: $%.2f", total));
            }
        }
    }

    /**
     * Confirm and complete the sale transaction
     */
    private void confirmSale() {
        // Debug output
        System.out.println("Confirm Sale called. Staff: " + (currentStaff != null ? currentStaff.getFull_name() : "null"));

        // Check if cart is empty
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(MainPanel,
                    "Shopping cart is empty. Add books before confirming sale.",
                    "Empty Cart",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if staff is logged in
        if (currentStaff == null) {
            JOptionPane.showMessageDialog(MainPanel,
                    "No staff logged in. Please login again.",
                    "Authentication Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirm sale
        double total = calculateTotalAmount();
        int confirm = JOptionPane.showConfirmDialog(MainPanel,
                "Confirm sale with total amount: $" + String.format("%.2f", total) + "?",
                "Confirm Sale",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // 1. Insert into sale table
            String saleQuery = "INSERT INTO sale (staff_id, sale_date) VALUES (?, ?)";
            int saleId = -1;

            try (PreparedStatement pstmt = con.prepareStatement(saleQuery, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, currentStaff.getStaffId());
                pstmt.setTimestamp(2, new Timestamp(new Date().getTime()));
                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        saleId = generatedKeys.getInt(1);
                    }
                }
            }

            if (saleId == -1) {
                throw new SQLException("Failed to generate sale ID");
            }

            // 2. Insert sale details and update stock
            String detailQuery = "INSERT INTO sale_detail (sale_id, book_id, quantity_sold, unit_price) VALUES (?, ?, ?, ?)";
            String updateStockQuery = "UPDATE book SET quantity = quantity - ? WHERE book_id = ?";

            try (PreparedStatement detailPstmt = con.prepareStatement(detailQuery);
                 PreparedStatement stockPstmt = con.prepareStatement(updateStockQuery)) {

                for (int i = 0; i < cartModel.getRowCount(); i++) {
                    int bookId = (int) cartModel.getValueAt(i, 0);
                    int quantity = (int) cartModel.getValueAt(i, 3);
                    double price = (double) cartModel.getValueAt(i, 2);

                    // Insert sale detail
                    detailPstmt.setInt(1, saleId);
                    detailPstmt.setInt(2, bookId);
                    detailPstmt.setInt(3, quantity);
                    detailPstmt.setDouble(4, price);
                    detailPstmt.addBatch();

                    // Update stock
                    stockPstmt.setInt(1, quantity);
                    stockPstmt.setInt(2, bookId);
                    stockPstmt.addBatch();
                }

                // Execute all batch operations
                detailPstmt.executeBatch();
                stockPstmt.executeBatch();
            }

            // Commit transaction
            con.commit();

            // Show success message
            JOptionPane.showMessageDialog(MainPanel,
                    "Sale completed successfully!\n" +
                            "Sale ID: " + saleId +
                            "\nTotal Amount: $" + String.format("%.2f", total),
                    "Sale Complete",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear cart and refresh
            clearCart();
            loadBooks();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(MainPanel,
                    "Error completing sale: " + e.getMessage(),
                    "Transaction Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calculate total amount from cart
     * @return total amount
     */
    private double calculateTotalAmount() {
        double total = 0.0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            total += (double) cartModel.getValueAt(i, 4);
        }
        return total;
    }

    /**
     * Cancel current sale - clear cart and refresh
     */
    private void cancelSale() {
        int confirm = JOptionPane.showConfirmDialog(MainPanel,
                "Are you sure you want to cancel the current sale? All cart items will be removed.",
                "Cancel Sale",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            clearCart();
            loadBooks();
            spinner1.setValue(1);
            textField1.setText("");
            comboBox1.setSelectedIndex(0);
        }
    }

    /**
     * Clear shopping cart
     */
    private void clearCart() {
        cartModel.setRowCount(0);
        updateTotalDisplay();
    }

    /**
     * Logout user and return to login screen
     */
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(MainPanel,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Get the parent window
            Window parentWindow = SwingUtilities.getWindowAncestor(MainPanel);

            // Close the dashboard window
            if (parentWindow != null) {
                parentWindow.dispose();
            }

            // Open login window
            SwingUtilities.invokeLater(() -> {
                try {
                    JFrame loginFrame = new JFrame("Bookstore POS - Login");
                    logIn loginForm = new logIn();
                    loginFrame.setContentPane(loginForm.getMainPanel());
                    loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    loginFrame.pack();
                    loginFrame.setLocationRelativeTo(null);
                    loginFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Error opening login screen: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }
}