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
    // Root Panel
    private JPanel MainPanel;

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
    private JLabel totalBooksLabel;
    private JLabel totalAmountLabel;

    // Buttons
    private JButton LogoutBtn;
    private JButton searchBtn;
    private JButton button1; // Add To Cart
    private JButton button2; // Remove Selected
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

    public NewSale() {
        this(null);
    }

    public NewSale(staff staff) {
        this.currentStaff = staff;

        System.out.println("=== NEW SALE CONSTRUCTOR ===");
        System.out.println("Received staff: " + (staff != null ? staff.getFull_name() : "NULL"));

        createUI();
        initializeTables();
        setupListeners();
        loadCategories();
        loadBooks();
        setupSpinner();
        displayUsername();
        isInitializing = false;
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    private void createUI() {
        MainPanel = new JPanel(new BorderLayout());
        MainPanel.setBackground(new Color(240, 240, 240));

        // ===== HEADER PANEL =====
        JPanel headerPanel = createHeaderPanel();
        MainPanel.add(headerPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        centerPanel.setBackground(new Color(240, 240, 240));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Left Panel - Books
        JPanel bookPanel = createBookPanel();
        centerPanel.add(bookPanel);

        // Right Panel - Cart
        JPanel cartPanel = createCartPanel();
        centerPanel.add(cartPanel);

        MainPanel.add(centerPanel, BorderLayout.CENTER);

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = createBottomPanel();
        MainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 30, 50));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Left: Logo
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        logoLabel = new JLabel("📚");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logoLabel.setForeground(Color.WHITE);

        storeNameLabel = new JLabel("Inkwell Books");
        storeNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        storeNameLabel.setForeground(Color.WHITE);

        leftPanel.add(logoLabel);
        leftPanel.add(storeNameLabel);

        // Right: User info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JPanel userPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        userPanel.setOpaque(false);

        Username = new JLabel("User");
        Username.setFont(new Font("Segoe UI", Font.BOLD, 14));
        Username.setForeground(Color.WHITE);

        accountLabel = new JLabel("Role");
        accountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accountLabel.setForeground(new Color(180, 180, 200));

        userPanel.add(Username);
        userPanel.add(accountLabel);

        LogoutBtn = new JButton("Logout");
        LogoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        LogoutBtn.setBackground(new Color(200, 50, 50));
        LogoutBtn.setForeground(Color.WHITE);
        LogoutBtn.setFocusPainted(false);
        LogoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        LogoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        rightPanel.add(userPanel);
        rightPanel.add(LogoutBtn);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Title
        JLabel titleLabel = new JLabel("📖 Books");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 30, 50));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(Color.WHITE);

        textField1 = new JTextField(15);
        textField1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField1.setPreferredSize(new Dimension(150, 30));
        textField1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        comboBox1 = new JComboBox<>();
        comboBox1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox1.setPreferredSize(new Dimension(120, 30));

        searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchBtn.setBackground(new Color(0, 120, 215));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(textField1);
        searchPanel.add(new JLabel("Category:"));
        searchPanel.add(comboBox1);
        searchPanel.add(searchBtn);

        // Quantity Panel
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        quantityPanel.setBackground(Color.WHITE);

        spinner1 = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spinner1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spinner1.setPreferredSize(new Dimension(60, 30));

        button1 = new JButton("Add to Cart");
        button1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button1.setBackground(new Color(46, 204, 113));
        button1.setForeground(Color.WHITE);
        button1.setFocusPainted(false);
        button1.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button1.setCursor(new Cursor(Cursor.HAND_CURSOR));

        quantityPanel.add(new JLabel("Quantity:"));
        quantityPanel.add(spinner1);
        quantityPanel.add(button1);

        // Table
        table1 = new JTable();
        table1.setRowHeight(30);
        table1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table1.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table1.getTableHeader().setBackground(new Color(52, 73, 94));
        table1.getTableHeader().setForeground(Color.WHITE);
        table1.getTableHeader().setPreferredSize(new Dimension(0, 30));
        table1.setSelectionBackground(new Color(173, 216, 230));

        JScrollPane scrollPane = new JScrollPane(table1);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Assemble
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(quantityPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Title
        JLabel titleLabel = new JLabel("🛒 Shopping Cart");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 30, 50));

        // Table
        table2 = new JTable();
        table2.setRowHeight(30);
        table2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table2.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table2.getTableHeader().setBackground(new Color(52, 73, 94));
        table2.getTableHeader().setForeground(Color.WHITE);
        table2.getTableHeader().setPreferredSize(new Dimension(0, 30));
        table2.setSelectionBackground(new Color(173, 216, 230));

        JScrollPane scrollPane = new JScrollPane(table2);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Remove Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);

        button2 = new JButton("Remove Selected");
        button2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button2.setBackground(new Color(200, 50, 50));
        button2.setForeground(Color.WHITE);
        button2.setFocusPainted(false);
        button2.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button2.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(button2);

        // Total Panel
        JPanel totalPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        totalPanel.setBackground(new Color(245, 245, 250));
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        totalBooksLabel = new JLabel("Total Books: 0");
        totalBooksLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalBooksLabel.setForeground(new Color(30, 30, 50));

        totalAmountLabel = new JLabel("Total Amount: $0.00");
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalAmountLabel.setForeground(new Color(0, 120, 215));

        totalPanel.add(totalBooksLabel);
        totalPanel.add(totalAmountLabel);

        // Assemble
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(totalPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelBtn.setBackground(new Color(150, 150, 150));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Confirm = new JButton("Confirm Sale");
        Confirm.setFont(new Font("Segoe UI", Font.BOLD, 13));
        Confirm.setBackground(new Color(46, 204, 113));
        Confirm.setForeground(Color.WHITE);
        Confirm.setFocusPainted(false);
        Confirm.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        Confirm.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomPanel.add(cancelBtn);
        bottomPanel.add(Confirm);

        return bottomPanel;
    }

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

        cartModel.addTableModelListener(e -> updateTotalDisplay());
    }

    private void setupSpinner() {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 999, 1);
        spinner1.setModel(spinnerModel);
    }

    private void setupListeners() {
        if (searchBtn != null) {
            searchBtn.addActionListener(e -> {
                if (!isInitializing) {
                    searchBooks();
                }
            });
        }

        if (textField1 != null) {
            textField1.addActionListener(e -> {
                if (!isInitializing) {
                    searchBooks();
                }
            });
        }

        if (comboBox1 != null) {
            comboBox1.addActionListener(e -> {
                if (!isInitializing) {
                    filterByCategory();
                }
            });
        }

        if (button1 != null) {
            button1.addActionListener(e -> addToCart());
        }

        if (button2 != null) {
            button2.addActionListener(e -> removeFromCart());
        }

        if (Confirm != null) {
            Confirm.addActionListener(e -> confirmSale());
        }

        if (cancelBtn != null) {
            cancelBtn.addActionListener(e -> cancelSale());
        }

        if (LogoutBtn != null) {
            LogoutBtn.addActionListener(e -> logout());
        }

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

    private void displayUsername() {
        if (currentStaff != null && Username != null) {
            Username.setText(currentStaff.getFull_name());
        }
        if (currentStaff != null && accountLabel != null) {
            accountLabel.setText(currentStaff.getRole());
        }
    }

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

    private void searchBooks() {
        String searchText = textField1.getText().trim();
        String selectedCategory = (String) comboBox1.getSelectedItem();

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

        if (!searchText.isEmpty()) {
            try {
                Integer.parseInt(searchText);
                query.append("AND b.book_id = ? ");
            } catch (NumberFormatException e) {
                query.append("AND b.title LIKE ? ");
            }
        }

        if (selectedCategory != null && !selectedCategory.equals("All Categories")) {
            query.append("AND c.category_name = ? ");
        }

        query.append("ORDER BY b.title");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query.toString())) {

            int paramIndex = 1;

            if (!searchText.isEmpty()) {
                try {
                    int bookId = Integer.parseInt(searchText);
                    pstmt.setInt(paramIndex++, bookId);
                } catch (NumberFormatException e) {
                    pstmt.setString(paramIndex++, "%" + searchText + "%");
                }
            }

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

    private void filterByCategory() {
        String selectedCategory = (String) comboBox1.getSelectedItem();
        String searchText = textField1.getText().trim();

        if (!searchText.isEmpty()) {
            searchBooks();
            return;
        }

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

    private void addToCart() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(MainPanel,
                    "Please select a book from the list.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookId = (int) table1.getValueAt(selectedRow, 0);
        String title = (String) table1.getValueAt(selectedRow, 1);
        double price = (double) table1.getValueAt(selectedRow, 3);
        int stock = (int) table1.getValueAt(selectedRow, 4);
        int quantity = (int) spinner1.getValue();

        if (quantity > stock) {
            JOptionPane.showMessageDialog(MainPanel,
                    "Insufficient stock! Available: " + stock,
                    "Stock Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean found = false;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            if ((int) cartModel.getValueAt(i, 0) == bookId) {
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
            double subtotal = price * quantity;
            Object[] row = {bookId, title, price, quantity, subtotal};
            cartModel.addRow(row);
        }

        updateTotalDisplay();
        table1.clearSelection();
        spinner1.setValue(1);
    }

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

    private void updateTotalDisplay() {
        double total = 0.0;
        int totalBooks = 0;

        for (int i = 0; i < cartModel.getRowCount(); i++) {
            total += (double) cartModel.getValueAt(i, 4);
            totalBooks += (int) cartModel.getValueAt(i, 3);
        }

        if (totalBooksLabel != null) {
            totalBooksLabel.setText("Total Books: " + totalBooks);
        }

        if (totalAmountLabel != null) {
            totalAmountLabel.setText(String.format("Total Amount: $%.2f", total));
        }
    }

    private void confirmSale() {
        System.out.println("=== CONFIRM SALE ===");
        System.out.println("Staff: " + (currentStaff != null ? currentStaff.getFull_name() : "NULL"));

        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(MainPanel,
                    "Shopping cart is empty. Add books before confirming sale.",
                    "Empty Cart",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentStaff == null) {
            JOptionPane.showMessageDialog(MainPanel,
                    "No staff logged in. Please login again.",
                    "Authentication Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

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

            String detailQuery = "INSERT INTO sale_detail (sale_id, book_id, quantity_sold, unit_price) VALUES (?, ?, ?, ?)";
            String updateStockQuery = "UPDATE book SET quantity = quantity - ? WHERE book_id = ?";

            try (PreparedStatement detailPstmt = con.prepareStatement(detailQuery);
                 PreparedStatement stockPstmt = con.prepareStatement(updateStockQuery)) {

                for (int i = 0; i < cartModel.getRowCount(); i++) {
                    int bookId = (int) cartModel.getValueAt(i, 0);
                    int quantity = (int) cartModel.getValueAt(i, 3);
                    double price = (double) cartModel.getValueAt(i, 2);

                    detailPstmt.setInt(1, saleId);
                    detailPstmt.setInt(2, bookId);
                    detailPstmt.setInt(3, quantity);
                    detailPstmt.setDouble(4, price);
                    detailPstmt.addBatch();

                    stockPstmt.setInt(1, quantity);
                    stockPstmt.setInt(2, bookId);
                    stockPstmt.addBatch();
                }

                detailPstmt.executeBatch();
                stockPstmt.executeBatch();
            }

            con.commit();

            JOptionPane.showMessageDialog(MainPanel,
                    "Sale completed successfully!\n" +
                            "Sale ID: " + saleId +
                            "\nTotal Amount: $" + String.format("%.2f", total),
                    "Sale Complete",
                    JOptionPane.INFORMATION_MESSAGE);

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

    private double calculateTotalAmount() {
        double total = 0.0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            total += (double) cartModel.getValueAt(i, 4);
        }
        return total;
    }

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

    private void clearCart() {
        cartModel.setRowCount(0);
        updateTotalDisplay();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(MainPanel,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Window parentWindow = SwingUtilities.getWindowAncestor(MainPanel);

            if (parentWindow != null) {
                parentWindow.dispose();
            }

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