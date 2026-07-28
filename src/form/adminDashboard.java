package form;

import DAO.bookDAO;
import DAO.categoryDAO;
import DAO.supplierDAO;
import model.book;
import model.category;
import model.staff;
import model.supplier;
import DAO.saleDetailDAO;
import model.saleDetail;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class adminDashboard {
    private JPanel mainPanel;
    private JLabel storeLogo;
    private JLabel avatarLabel;
    private JButton logoutBtn;
    private JLabel staffNameLabel;
    private JLabel staffroleLabel;
    private JTabbedPane dashboardTab;

    // Book
    private JPanel booksPanel;
    private JButton addBookBtn;
    private JLabel totalTitleLabel;
    private JLabel totalStockLabel;
    private JLabel totalLowStock;
    private JTextField bookSearchField;
    private JComboBox<String> categoryFilterCombo;
    private JButton updateBookBtn;
    private JButton deleteBookBtn;
    private JTable bookTable;

    // Category
    private JPanel categoriesPanel;
    private JLabel totalCategoryLabel;
    private JPanel categoryTopPanel;
    private JButton addCategoryButton;
    private JPanel categoryGridPanel;
    private category selectedCategory = null;
    private JPanel selectedCard = null;

    // Supplier
    private JTextField searchSuppliersTextField;
    private JTable supplierTable;
    private JButton addSupplierButton;
    private JPanel supplierPanel;
    private JButton updateSupplierBtn;
    private JButton deleteSupplierBtn;

    // Low stock
    private JPanel lowStockPanel;
    private JLabel totalLowStockItemLabel;
    private JLabel outOfStockLabel;
    private JLabel criticalBookLabel;
    private JTable lowStockTable;
    private JButton deleteBookButton;
    private JButton updateBookButton;
    private JButton deleteCategoryBtn;
    private JButton updateCategoryBtn;
    private JTable saleDetailTable;
    private JButton updateButton2;
    private JButton deleteButton2;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    // Load the statistic
    public adminDashboard(staff loggedInStaff) {
        staffNameLabel.setText(loggedInStaff.getFull_name());
        staffroleLabel.setText(loggedInStaff.getRole());

        loadDashboardStatistics();
        loadBookTable();
        loadSupplierTable();
        loadCategoryFilter();
        bookSearchField.setText("Search book by title...");
        bookSearchField.setForeground(Color.GRAY);

        bookSearchField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent e) {

                if (bookSearchField.getText().equals("Search book by title...")) {
                    bookSearchField.setText("");
                    bookSearchField.setForeground(Color.BLACK);
                }

            }

            public void focusLost(java.awt.event.FocusEvent e) {

                if (bookSearchField.getText().isEmpty()) {
                    bookSearchField.setText("Search book by title...");
                    bookSearchField.setForeground(Color.GRAY);
                }

            }

        });
        searchSuppliersTextField.setText("Search supplier by name...");
        searchSuppliersTextField.setForeground(Color.GRAY);

        searchSuppliersTextField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent e) {

                if (searchSuppliersTextField.getText().equals("Search supplier by name...")) {
                    searchSuppliersTextField.setText("");
                    searchSuppliersTextField.setForeground(Color.BLACK);
                }

            }

            public void focusLost(java.awt.event.FocusEvent e) {

                if (searchSuppliersTextField.getText().isEmpty()) {
                    searchSuppliersTextField.setText("Search supplier by name...");
                    searchSuppliersTextField.setForeground(Color.GRAY);
                }

            }

        });

        categoryGridPanel.setLayout(
                new FlowLayout(FlowLayout.LEFT, 15, 15)
        );

        loadCategoryCards();
        loadLowStockStatistics();
        loadLowStockTable();

        addBookBtn.addActionListener(e -> openAddBookForm());
        updateBookButton.addActionListener(e ->openUpdateBookForm());
        deleteBookButton.addActionListener(e->openDeleteBookDialog());

        addCategoryButton.addActionListener(e -> openAddCategoryForm());
        updateCategoryBtn.addActionListener(e -> openUpdateCategory());
        deleteCategoryBtn.addActionListener(e -> openDeleteCategory());

        addSupplierButton.addActionListener(e-> openAddSupplierForm());
        updateSupplierBtn.addActionListener(e -> openUpdateSupplierForm());
        deleteSupplierBtn.addActionListener(e -> openDeleteSupplierForm());

        logoutBtn.addActionListener(e -> openLogoutForm());

        //typing listen
        bookSearchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                searchBooks();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                searchBooks();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                searchBooks();
            }
        });
        categoryFilterCombo.addActionListener(e -> searchBooks());

        searchSuppliersTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                searchSupplier();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                searchSupplier();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                searchSupplier();
            }
        });

        loadSupplierTable();
        loadSaleDetailTable();

    }
//
//    private void loadDashboardStatistics() {
//        totalTitleLabel.setText(String.valueOf(bookDAO.getTotalBooks()));
//        totalStockLabel.setText(String.valueOf(bookDAO.getTotalStock()));
//        totalCategoryLabel.setText(String.valueOf(categoryDAO.getTotalCategories()));
//        totalLowStock.setText(String.valueOf(bookDAO.getLowStockBooks().size()));
//    }
private void loadDashboardStatistics() {

    // Book tab
    totalTitleLabel.setText(String.valueOf(bookDAO.getTotalBooks()));
    totalStockLabel.setText(String.valueOf(bookDAO.getTotalStock()));
    totalLowStock.setText(String.valueOf(bookDAO.getLowStockBooks().size()));

    // Category tab
    totalCategoryLabel.setText(String.valueOf(categoryDAO.getTotalCategories()));
}
//private void loadDashboardStatistics() {
//
//    int total = categoryDAO.getTotalCategories();
//
//    System.out.println("loadDashboardStatistics() called");
//    System.out.println("Total categories = " + total);
//
//    totalTitleLabel.setText(String.valueOf(bookDAO.getTotalBooks()));
//    totalStockLabel.setText(String.valueOf(bookDAO.getTotalStock()));
//    totalLowStock.setText(String.valueOf(bookDAO.getLowStockBooks().size()));
//    totalCategoryLabel.setText(String.valueOf(total));
//}


    // Load the book table
    private void loadBookTable(List<book> books) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Title", "Author", "Price", "Quantity", "Category", "Supplier"}, 0);
        bookTable.setModel(model);
        bookTable.setRowHeight(35);
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Title
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(180); // Author
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Price
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Quantity
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Category
        bookTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Supplier

        bookTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        bookTable.getTableHeader().setPreferredSize(
                new Dimension(0,35)
        );

        for (book b : books) {
            model.addRow(new Object[]{
                    b.getBookId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getPrice(),
                    b.getStockQuantity(),
                    b.getCategoryName(),
                    b.getSupplierName()
            });

        }
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        for(int i = 0; i < bookTable.getColumnCount(); i++){
            bookTable.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(center);
        }
    }
    private void loadBookTable() {
        loadBookTable(bookDAO.getAllBooks());
    }
    private void openAddBookForm() {

        JFrame frame = new JFrame("Add Book");

        addBookForm form = new addBookForm(() -> {

            loadDashboardStatistics();
            loadBookTable();

        });

        frame.setContentPane(form.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private void openAddCategoryForm() {

        JFrame frame = new JFrame("Add Category");

        addCategoryForm form = new addCategoryForm(() -> {

            loadCategoryCards();
            loadCategoryFilter();
            loadDashboardStatistics();
            mainPanel.revalidate();
            mainPanel.repaint();
            selectedCategory = null;
            selectedCard = null;

        });

        frame.setContentPane(form.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(mainPanel);
        frame.setVisible(true);

    }

private void openUpdateBookForm() {

    int row = bookTable.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(mainPanel,
                "Please select a book to update.");
        return;
    }

    int bookId = (int) bookTable.getValueAt(row, 0);

    bookDAO dao = new bookDAO();
    book selectedBook = dao.getBookById(bookId);

    editBook dialog = new editBook(selectedBook);
    dialog.pack();                     // <-- Important
    dialog.setLocationRelativeTo(mainPanel);
    dialog.setVisible(true);

////    loadBookTable();
//    loadDashboardStatistics();
//    loadBookTable();

    loadDashboardStatistics();
    loadBookTable();
    loadLowStockStatistics();
    loadLowStockTable();
}

    private void openDeleteBookDialog() {

        int row = bookTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Please select a book to delete."
            );
            return;
        }

        int bookId = (int) bookTable.getValueAt(row, 0);

        bookDAO dao = new bookDAO();
        book selectedBook = dao.getBookById(bookId);

        deleteBook dialog = new deleteBook(selectedBook, () -> {

            loadDashboardStatistics();
            loadBookTable();

        });

        JFrame frame = new JFrame("Delete Book");
        frame.setContentPane(dialog.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(mainPanel);
        frame.setVisible(true);


    }

    private void openLogoutForm() {

//        logOut dialog = new logOut();
        Window dashboardWindow =
                SwingUtilities.getWindowAncestor(mainPanel);

        logOut dialog = new logOut(dashboardWindow);

        dialog.pack();
        dialog.setLocationRelativeTo(mainPanel);
        dialog.setVisible(true);

    }

    private void loadSupplierTable()
    {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Name", "Contact Number", "Email", "Address"}, 0);
        supplierTable.setModel(model);
        supplierTable.setRowHeight(35);
        supplierTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        supplierTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Title
        supplierTable.getColumnModel().getColumn(2).setPreferredWidth(180); // Author
        supplierTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Price
        supplierTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Quantity
        supplierTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        supplierTable.getTableHeader().setPreferredSize(
                new Dimension(0,35)
        );

        List<supplier> suppliers = supplierDAO.getAllSuppliers();
        for (supplier s : suppliers) {
//            model.addRow(new Object[]{
//                    b.getBookId(), b.getTitle(), b.getAuthor(),
//                    b.getPrice(), b.getStockQuantity(),
//                    b.getCategoryId(), b.getSupplierId(), "Edit"
//            });
            model.addRow(new Object[]{
                    s.getSupplierId(),
                    s.getSupplierName(),
                    s.getContactNumber(),
                    s.getEmail(),
                    s.getAddress()
            });

        }
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        for(int i = 0; i < supplierTable.getColumnCount(); i++){
            supplierTable.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(center);
        }

    }
    private void loadCategoryFilter() {

        categoryFilterCombo.removeAllItems();

        categoryFilterCombo.addItem("All Categories");

        List<model.category> categories = categoryDAO.getAllCategories();

        for (model.category c : categories) {

            categoryFilterCombo.addItem(c.getCategoryName());

        }
    }
    private void searchBooks() {

        String keyword = bookSearchField.getText().trim();

        if(keyword.equals("Search book by title...")){
            keyword = "";
        }

        String category = (String) categoryFilterCombo.getSelectedItem();
        if (category == null) {
            return;
        }

        List<book> books = bookDAO.searchAndFilterBooks(keyword, category);

        loadBookTable(books);
    }
private JPanel createCategoryCard(category c) {

    JPanel card = new JPanel();

    // smaller card size
    card.setPreferredSize(new Dimension(300, 120));

    // add space inside the card (padding)
    card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
    ));

    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBackground(Color.WHITE);


    JLabel title = new JLabel(c.getCategoryName());
    title.setFont(new Font("Segoe UI", Font.BOLD, 16));
    title.setAlignmentX(Component.LEFT_ALIGNMENT);


    JLabel description = new JLabel(
            "<html><body style='width:160px'>"
                    + c.getDescription()
                    + "</body></html>"
    );
    description.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    description.setAlignmentX(Component.LEFT_ALIGNMENT);


    categoryDAO dao = new categoryDAO();
    int bookCount = dao.getBookCountByCategory(c.getCategoryId());

    JLabel count = new JLabel(bookCount + " Books");
    count.setForeground(new Color(34, 139, 34));
    count.setFont(new Font("Segoe UI", Font.BOLD, 13));
    count.setAlignmentX(Component.LEFT_ALIGNMENT);


    card.add(title);
    card.add(Box.createVerticalStrut(8));
    card.add(description);
    card.add(Box.createVerticalGlue());
    card.add(count);

    card.setCursor(new Cursor(Cursor.HAND_CURSOR));


    card.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {

            selectedCategory = c;

            if (selectedCard != null) {
                selectedCard.setBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY)
                );
            }

            selectedCard = card;

            card.setBorder(
                    BorderFactory.createLineBorder(
                            new Color(0,120,215),
                            3
                    )
            );
        }
    });

    return card;
}
    private void loadCategoryCards() {

        categoryGridPanel.removeAll();

        List<category> categories =
                categoryDAO.getAllCategories();

        for(category c : categories){

            JPanel card =
                    createCategoryCard(c);

            categoryGridPanel.add(card);

        }

        categoryGridPanel.revalidate();

        categoryGridPanel.repaint();

    }
    private void loadLowStockStatistics() {

        // Total low stock items
        int lowStockCount = bookDAO.getLowStockBooks().size();

        totalLowStockItemLabel.setText(
                String.valueOf(lowStockCount)
        );


        // Out of stock
        int outStock = bookDAO.getOutOfStockBooks();

        outOfStockLabel.setText(
                String.valueOf(outStock)
        );


        // Critical book
        book criticalBook = bookDAO.getCriticalBook();

        if (criticalBook != null) {

            criticalBookLabel.setText(
                    criticalBook.getTitle()
                            + " ("
                            + criticalBook.getStockQuantity()
                            + ")"
            );

        } else {

            criticalBookLabel.setText("No Data");

        }
    }
    private void loadLowStockTable() {

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Title",
                        "Author",
                        "Quantity",
                        "Category",
                        "Supplier"
                }, 0
        );

        lowStockTable.setModel(model);

        lowStockTable.setRowHeight(35);
        lowStockTable.getTableHeader().setPreferredSize(
                new Dimension(0,35)
        );


        List<book> books = bookDAO.getLowStockBooks();


        for (book b : books) {

            model.addRow(new Object[]{
                    b.getBookId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getStockQuantity(),
                    b.getCategoryName(),
                    b.getSupplierName()
            });

        }


        // Center text
        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();

        center.setHorizontalAlignment(JLabel.CENTER);


        for(int i = 0; i < lowStockTable.getColumnCount(); i++){

            lowStockTable.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(center);

        }


        // Column sizes
        lowStockTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(50);

        lowStockTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(180);

        lowStockTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(150);

        lowStockTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(80);

        lowStockTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(120);

        lowStockTable.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(120);
    }
    private void openUpdateCategory() {

        if (selectedCategory == null) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Please select a category."
            );

            return;
        }

        editCategory dialog = new editCategory(
                selectedCategory,
                () -> {

                    loadCategoryCards();
                    loadCategoryFilter();
                    loadBookTable();              // refresh book table
                    loadDashboardStatistics();

                    mainPanel.revalidate();
                    mainPanel.repaint();
                    selectedCategory = null;
                    selectedCard = null;

                }
        );

        dialog.pack();
        dialog.setLocationRelativeTo(mainPanel);
        dialog.setVisible(true);
    }
    private void openDeleteCategory() {

        if (selectedCategory == null) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Please select a category."
            );

            return;
        }

        JFrame frame = new JFrame("Delete Category");

        deleteCategory form = new deleteCategory(
                selectedCategory,
                () -> {

                    loadCategoryCards();
                    loadCategoryFilter();
                    loadBookTable();              // refresh book table
                    loadDashboardStatistics();
                    mainPanel.revalidate();
                    mainPanel.repaint();
                    selectedCategory = null;
                    selectedCard = null;
                }
        );

        frame.setContentPane(form.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(mainPanel);
        frame.setVisible(true);
    }
    private void openUpdateSupplierForm(){

        int row = supplierTable.getSelectedRow();

        if(row == -1){

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Please select a supplier to update."
            );

            return;
        }


        int supplierId = (int)supplierTable.getValueAt(row,0);


        supplierDAO dao = new supplierDAO();

        supplier selectedSupplier =
                dao.getSupplierById(supplierId);


        editSupplier dialog =
                new editSupplier(selectedSupplier);


        dialog.pack();
        dialog.setLocationRelativeTo(mainPanel);
        dialog.setVisible(true);


        // refresh table after update
        loadSupplierTable();
    }
    private void openDeleteSupplierForm(){

        int row = supplierTable.getSelectedRow();


        if(row == -1){

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Please select a supplier to delete."
            );

            return;
        }


        int supplierId =
                (int)supplierTable.getValueAt(row,0);


        supplierDAO dao = new supplierDAO();


        supplier selectedSupplier =
                dao.getSupplierById(supplierId);



        JFrame frame = new JFrame("Delete Supplier");


        deleteSupplier form =
                new deleteSupplier(
                        selectedSupplier,
                        () -> {

                            loadSupplierTable();

                        }
                );


        frame.setContentPane(form.getMainPanel());

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.pack();

        frame.setLocationRelativeTo(mainPanel);

        frame.setVisible(true);
    }
    private void openAddSupplierForm(){

        JFrame frame = new JFrame("Add Supplier");


        addSupplierForm form =
                new addSupplierForm(() -> {

                    loadSupplierTable();

                });


        frame.setContentPane(form.getMainPanel());


        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );


        frame.pack();

        frame.setLocationRelativeTo(mainPanel);

        frame.setVisible(true);
    }
    private void searchSupplier() {

        String keyword = searchSuppliersTextField.getText().trim();

        if(keyword.equals("Search supplier by name...")){
            keyword = "";
        }
        supplierDAO dao = new supplierDAO();

        List<supplier> suppliers;

        if(keyword.isEmpty()){

            suppliers = supplierDAO.getAllSuppliers();

        }else{

            suppliers = dao.searchSuppliers(keyword);

        }


        DefaultTableModel model =
                (DefaultTableModel) supplierTable.getModel();

        model.setRowCount(0); // clear old rows


        for(supplier s : suppliers){

            model.addRow(new Object[]{
                    s.getSupplierId(),
                    s.getSupplierName(),
                    s.getContactNumber(),
                    s.getEmail(),
                    s.getAddress()
            });

        }
    }
    private void loadSaleDetailTable(){

        DefaultTableModel model =
                new DefaultTableModel(
                        new Object[]{
                                "Sale ID",
                                "Book Title",
                                "Quantity",
                                "Unit Price",
                                "Date",
                                "Cashier"
                        },0
                );


        saleDetailTable.setModel(model);


        List<saleDetail> details =
                saleDetailDAO.getAllSaleDetails();


        for(saleDetail sd : details){

            model.addRow(new Object[]{
                    sd.getSaleId(),
                    sd.getBookTitle(),
                    sd.getQuantitySold(),
                    sd.getUnitPrice(),
                    sd.getSaleDate(),
                    sd.getCashierName()
            });

        }
    }
}
