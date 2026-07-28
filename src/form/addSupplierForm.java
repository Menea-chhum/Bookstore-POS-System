package form;

import DAO.supplierDAO;
import model.supplier;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class addSupplierForm {

    private JPanel mainPanel;
    private JButton a️SaveAllSuppliersButton;
    private JButton clearAllSupplierListButton;
    private JTable addSupplierTable;
    private JTextField supplierNameInput;
    private JTextField supplierNumberInput;
    private JTextField supplierEmailInput;
    private JTextField supplierAddressInput;
    private JButton addSupplierToListButton;
    private JLabel supplierErrMsg;
    private JButton deleteSupplierListButton;
    private DefaultTableModel tableModel;


    private Runnable refresh;


    public addSupplierForm(Runnable refresh){

        this.refresh = refresh;


        tableModel = new DefaultTableModel(
                new Object[]{
                        "Name",
                        "Contact Number",
                        "Email",
                        "Address"
                },0
        );


        addSupplierTable.setModel(tableModel);


        addSupplierToListButton.addActionListener(e -> addSupplierToTable());


        a️SaveAllSuppliersButton.addActionListener(e -> saveAllSuppliers());


        clearAllSupplierListButton.addActionListener(e -> clearFields());
        deleteSupplierListButton.addActionListener(e -> {

            int row = addSupplierTable.getSelectedRow();


            if(row != -1){

                tableModel.removeRow(row);

            }else{

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Please select a supplier from the list."
                );

            }

        });
//        clearAllSupplierListButton.addActionListener(e -> {
//
//            int confirm = JOptionPane.showConfirmDialog(
//                    mainPanel,
//                    "Clear all suppliers from the list?",
//                    "Confirm",
//                    JOptionPane.YES_NO_OPTION
//            );
//
//            if(confirm == JOptionPane.YES_OPTION){
//
//                tableModel.setRowCount(0);
//
//            }
//
//        });
        clearAllSupplierListButton.addActionListener(e -> {

            tableModel.setRowCount(0);

        });

    }

    private void addSupplierToTable(){

        String name =
                supplierNameInput.getText().trim();

        String number =
                supplierNumberInput.getText().trim();

        String email =
                supplierEmailInput.getText().trim();

        String address =
                supplierAddressInput.getText().trim();



        if(name.isEmpty() ||
                number.isEmpty() ||
                email.isEmpty() ||
                address.isEmpty()){


            supplierErrMsg.setText(
                    "Please fill all fields."
            );

            return;
        }



        tableModel.addRow(new Object[]{
                name,
                number,
                email,
                address
        });


        clearFields();


        supplierErrMsg.setText(
                "Supplier added to list."
        );
    }

    private void saveAllSuppliers(){

        supplierDAO dao = new supplierDAO();


        if(tableModel.getRowCount() == 0){

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "No supplier to save."
            );

            return;
        }



        boolean allSuccess = true;


        for(int i = 0; i < tableModel.getRowCount(); i++){


            supplier s = new supplier(
                    0,
                    tableModel.getValueAt(i,0).toString(),
                    tableModel.getValueAt(i,1).toString(),
                    tableModel.getValueAt(i,2).toString(),
                    tableModel.getValueAt(i,3).toString()
            );


            boolean success =
                    dao.addSupplier(s);


            if(!success){
                allSuccess = false;
            }
        }



        if(allSuccess){

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "All suppliers saved successfully!"
            );


            refresh.run();


            Window window =
                    SwingUtilities.getWindowAncestor(mainPanel);

            window.dispose();


        }else{

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Some suppliers failed to save."
            );

        }

    }



    private void clearFields(){

        supplierNameInput.setText("");
        supplierNumberInput.setText("");
        supplierEmailInput.setText("");
        supplierAddressInput.setText("");

        supplierErrMsg.setText("");

    }



    public JPanel getMainPanel(){

        return mainPanel;

    }

}