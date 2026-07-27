package form;

import DAO.bookDAO;
import DAO.categoryDAO;
import model.book;
import model.staff;

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
    private JLabel totalCategoryLabel;
    private JLabel totalLowStock;
    private JTextField bookSearchField;
    private JComboBox<String> categoryFilterCombo;
    private JButton updateBookBtn;
    private JButton deleteBookBtn;
    private JTable bookTable;

    // Category
    private JPanel categoriesPanel;
    private JPanel categoryTopPanel;
    private JButton addCategoryButton;
    private JTextField categorySearchField;
    private JPanel categoryGridPanel;

    // Supplier
    private JTextField searchSuppliersTextField;
    private JTable supplierTable;
    private JButton addSupplierButton;
    private JPanel supplierPanel;
    private JButton updateSupplierBtn;
    private JButton deleteSupplierBtn;

    // Low stock
    private JPanel lowStockPanel;
    private JLabel lowStockItemLabel;
    private JLabel lowestStockTitleLabel;
    private JLabel alertMessageLabel;
    private JTable lowStockTable;
    private JButton deleteButton;
    private JButton updateButton;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    // Load the statistic
    public adminDashboard(staff loggedInStaff) {
        staffNameLabel.setText(loggedInStaff.getFull_name());
        staffroleLabel.setText(loggedInStaff.getRole());

        loadDashboardStatistics();
        loadBookTable();

        addBookBtn.addActionListener(e -> openAddBookForm());
        updateButton.addActionListener(e ->openUpdateForm());
//        deleteButton.addActionListener(e -> openDeleteDiaglog());
        logoutBtn.addActionListener(e -> openLogoutForm());
        deleteButton.addActionListener(e -> openDeleteDialog());
    }

    private void loadDashboardStatistics() {
        totalTitleLabel.setText(String.valueOf(bookDAO.getTotalBooks()));
        totalStockLabel.setText(String.valueOf(bookDAO.getTotalStock()));
        totalCategoryLabel.setText(String.valueOf(categoryDAO.getTotalCategories()));
        totalLowStock.setText(String.valueOf(bookDAO.getLowStockBooks().size()));
    }

    // Load the book table
    private void loadBookTable() {
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

        List<book> books = bookDAO.getAllBooks();
        for (book b : books) {
//            model.addRow(new Object[]{
//                    b.getBookId(), b.getTitle(), b.getAuthor(),
//                    b.getPrice(), b.getStockQuantity(),
//                    b.getCategoryId(), b.getSupplierId(), "Edit"
//            });
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

//    private void openUpdateForm(){
//        JFrame frame = new JFrame("Update Book");
//
//        editBook form = new editBook();
//
//        frame.setContentPane(form.getContentPane());
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }
//private void openUpdateForm() {
//
//    int row = bookTable.getSelectedRow();
//
//    if (row == -1) {
//        JOptionPane.showMessageDialog(
//                mainPanel,
//                "Please select a book to update."
//        );
//        return;
//    }
//
//    int bookId = (int) bookTable.getValueAt(row, 0);
//
//    bookDAO dao = new bookDAO();
//    book selectedBook = dao.getBookById(bookId);
//
//    editBook dialog = new editBook(selectedBook);
//
//    dialog.setLocationRelativeTo(mainPanel);
//    dialog.setVisible(true);
//
//    loadBookTable();
//}
private void openUpdateForm() {

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

//    loadBookTable();
    loadDashboardStatistics();
    loadBookTable();
}

    private void openDeleteDialog() {

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

        deleteBook dialog = new deleteBook(selectedBook);

        JFrame frame = new JFrame("Delete Book");
        frame.setContentPane(dialog.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(mainPanel);
        frame.setVisible(true);

        loadDashboardStatistics();
        loadBookTable();

    }

    private void openLogoutForm(){
        JFrame frame = new JFrame("Log out");
        logOut form = new logOut();
        frame.setContentPane(form.getContentPane());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
