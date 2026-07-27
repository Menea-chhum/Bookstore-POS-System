//package form;
//
//import model.category;
//import model.supplier;
//
//import javax.swing.*;

package form;

import DAO.bookDAO;
import DAO.categoryDAO;
import DAO.supplierDAO;
import model.book;
import model.category;
import model.supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class addBookForm {
    private JPanel mainPanel;
    private JComboBox<category> categoryCombo;
    private JComboBox<supplier> supplierCombo;
    private JTextField titleInput;
    private JTextField authorInput;
    private JTextField priceInput;
    private JTextField stockQuantityInput;
    private JButton addToListButton;
    private JButton a️SaveAllBookButton;
    private JButton clearAllButton;
    private JLabel errMsg;
    private JTable table1;
    private JButton deleteButton;

    private DefaultTableModel model;
    private Runnable refreshCallback;

    public JPanel getMainPanel(){
        return mainPanel;
    }

    private List<book> pendingBooks = new ArrayList<>();

    private categoryDAO categoryDAO = new categoryDAO();
    private supplierDAO supplierDAO = new supplierDAO();
    private bookDAO bookDAO = new bookDAO();

    //constructor
    public addBookForm(Runnable refreshCallback) {

        this.refreshCallback = refreshCallback;

        initializeTable();

        loadCategories();

        loadSuppliers();

        registerEvents();
    }
    private void initializeTable() {

        model = new DefaultTableModel(
                new Object[]{
                        "Title",
                        "Author",
                        "Price",
                        "Quantity",
                        "Category",
                        "Supplier"
                },0);

        table1.setModel(model);

        table1.setRowHeight(30);

        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }
    private void loadCategories() {

        categoryCombo.removeAllItems();

        List<category> categories = categoryDAO.getAllCategories();

        for(category c : categories){

            categoryCombo.addItem(c);

        }

    }
    private void loadSuppliers(){

        supplierCombo.removeAllItems();

        List<supplier> suppliers = supplierDAO.getAllSuppliers();

        for(supplier s : suppliers){

            supplierCombo.addItem(s);

        }

    }
    private void registerEvents(){

        addToListButton.addActionListener(e -> addBookToList());
//
        deleteButton.addActionListener(e -> deleteSelectedBook());
//
        clearAllButton.addActionListener(e -> clearAllBooks());

        a️SaveAllBookButton.addActionListener(e -> saveAllBooks());

    }
    private void addBookToList() {

        errMsg.setText("");

        String title = titleInput.getText().trim();
        String author = authorInput.getText().trim();

        if (title.isEmpty()) {
            errMsg.setText("Please enter the book title.");
            return;
        }

        if (author.isEmpty()) {
            errMsg.setText("Please enter the author.");
            return;
        }

        double price;

        try {
            price = Double.parseDouble(priceInput.getText().trim());

            if (price <= 0) {
                errMsg.setText("Price must be greater than 0.");
                return;
            }

        } catch (NumberFormatException e) {
            errMsg.setText("Invalid price.");
            return;
        }

        int quantity;

        try {
            quantity = Integer.parseInt(stockQuantityInput.getText().trim());

            if (quantity < 0) {
                errMsg.setText("Quantity cannot be negative.");
                return;
            }

        } catch (NumberFormatException e) {
            errMsg.setText("Invalid quantity.");
            return;
        }

        category c = (category) categoryCombo.getSelectedItem();
        supplier s = (supplier) supplierCombo.getSelectedItem();

        if (c == null) {
            errMsg.setText("Please select a category.");
            return;
        }

        if (s == null) {
            errMsg.setText("Please select a supplier.");
            return;
        }

        // Duplicate check
        for (book b : pendingBooks) {

            if (b.getTitle().equalsIgnoreCase(title)) {

                errMsg.setText("This book is already in the list.");
                return;
            }
        }

        book newBook = new book(
                0,
                title,
                author,
                price,
                quantity,
                c.getCategoryId(),
                s.getSupplierId()
        );

        newBook.setCategoryName(c.getCategoryName());
        newBook.setSupplierName(s.getSupplierName());

        pendingBooks.add(newBook);

        refreshTable();
        clearInputFields();

        errMsg.setText("");

    }
    private void refreshTable() {

        model.setRowCount(0);

        for (book b : pendingBooks) {

            model.addRow(new Object[]{
                    b.getTitle(),
                    b.getAuthor(),
                    b.getPrice(),
                    b.getStockQuantity(),
                    b.getCategoryName(),
                    b.getSupplierName()
            });

        }

    }
    private void clearInputFields() {

        titleInput.setText("");
        authorInput.setText("");
        priceInput.setText("");
        stockQuantityInput.setText("");

        if (categoryCombo.getItemCount() > 0)
            categoryCombo.setSelectedIndex(0);

        if (supplierCombo.getItemCount() > 0)
            supplierCombo.setSelectedIndex(0);

        titleInput.requestFocus();

    }
    private void deleteSelectedBook() {

        int selectedRow = table1.getSelectedRow();

        if (selectedRow == -1) {
            errMsg.setText("Please select a book to delete.");
            return;
        }

        pendingBooks.remove(selectedRow);

        refreshTable();

        errMsg.setText("");
    }
    private void clearAllBooks() {

        if (pendingBooks.isEmpty()) {
            errMsg.setText("The list is already empty.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                mainPanel,
                "Clear all books from the list?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            pendingBooks.clear();

            refreshTable();

            errMsg.setText("");

        }

    }
    private void saveAllBooks() {

        if (pendingBooks.isEmpty()) {

            errMsg.setText("There are no books to save.");
            return;
        }

        int success = 0;

        for (book b : pendingBooks) {

            boolean result = bookDAO.addBook(b);

            System.out.println("Saving: " + b.getTitle() + " -> " + result);

            if (result) {
                success++;
            }

        }

        if (success == pendingBooks.size()) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    success + " book(s) added successfully!"
            );

            pendingBooks.clear();

            refreshTable();

            clearInputFields();

            errMsg.setText("");

            if (refreshCallback != null) {
                refreshCallback.run();
            }

        } else {

            errMsg.setText("Some books could not be saved.");

        }

    }

}
