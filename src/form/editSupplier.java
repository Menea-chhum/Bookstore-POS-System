package form;

import DAO.supplierDAO;
import model.supplier;

import javax.swing.*;
import java.awt.event.*;

public class editSupplier extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonSaveChange;
    private JTextField supplierNameField;
    private JTextField contactNumField;
    private JTextField emailField;
    private JTextField addressField;
    private supplier selectedSupplier;

    public JPanel getContentPane(){
        return contentPane;
    }


    public editSupplier(supplier supplier) {

        setContentPane(contentPane);
        setModal(true);

        this.selectedSupplier = supplier;

        // show old data in fields
        supplierNameField.setText(supplier.getSupplierName());
        contactNumField.setText(supplier.getContactNumber());
        emailField.setText(supplier.getEmail());
        addressField.setText(supplier.getAddress());


        buttonCancel.addActionListener(e -> onCancel());

        buttonSaveChange.addActionListener(e -> onSaveChange());


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    private void onCancel() {
        // add your code here
        dispose();
    }

    private void onSaveChange() {

        String name = supplierNameField.getText().trim();
        String contact = contactNumField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();


        supplier updatedSupplier = new supplier(
                selectedSupplier.getSupplierId(),
                name,
                contact,
                email,
                address
        );


        supplierDAO dao = new supplierDAO();

        boolean success = dao.updateSupplier(updatedSupplier);


        if(success){

            JOptionPane.showMessageDialog(
                    this,
                    "Supplier updated successfully!"
            );

            dispose();

        }else{

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update supplier."
            );
        }
    }

}
