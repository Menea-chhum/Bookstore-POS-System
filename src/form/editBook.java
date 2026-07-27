package form;

import DAO.bookDAO;
import DAO.categoryDAO;
import DAO.supplierDAO;
import model.book;
import model.category;
import model.supplier;


import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class editBook extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonSaveChange;
    private JComboBox categoryCombo;
    private JComboBox supplierCombo;
    private JTextField bookTitleField;
    private JTextField authorField;
    private JTextField priceField;
    private JTextField stockField;
    private JLabel errMsg;

    private book currentBook;
    private bookDAO dao = new bookDAO();

    public editBook(book b) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);

        loadCategories();
        loadSuppliers();

        currentBook = b;

        bookTitleField.setText(b.getTitle());
        authorField.setText(b.getAuthor());
        priceField.setText(String.valueOf(b.getPrice()));
        stockField.setText(String.valueOf(b.getStockQuantity()));

        for (int i = 0; i < categoryCombo.getItemCount(); i++) {

            category c = (category) categoryCombo.getItemAt(i);

            if (c.getCategoryId() == b.getCategoryId()) {

                categoryCombo.setSelectedIndex(i);
                break;

            }
        }

        for (int i = 0; i < supplierCombo.getItemCount(); i++) {

            supplier s = (supplier) supplierCombo.getItemAt(i);

            if (s.getSupplierId() == b.getSupplierId()) {

                supplierCombo.setSelectedIndex(i);
                break;

            }
        }

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        buttonSaveChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSaveChange();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here
        dispose();
    }

    private void onSaveChange() {

        try {

            currentBook.setTitle(bookTitleField.getText().trim());
            currentBook.setAuthor(authorField.getText().trim());
            currentBook.setPrice(Double.parseDouble(priceField.getText()));
            currentBook.setStockQuantity(Integer.parseInt(stockField.getText()));

            category c = (category) categoryCombo.getSelectedItem();
            supplier s = (supplier) supplierCombo.getSelectedItem();

            currentBook.setCategoryId(c.getCategoryId());
            currentBook.setSupplierId(s.getSupplierId());

            if (dao.updateBook(currentBook)) {

                JOptionPane.showMessageDialog(this, "Book updated successfully.");
                dispose();

            } else {

                errMsg.setText("Update failed.");

            }

        } catch (NumberFormatException e) {

            errMsg.setText("Price and Quantity must be numbers.");

        }

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

}
