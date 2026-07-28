package form;

import DAO.supplierDAO;
import model.supplier;

import javax.swing.*;
import java.awt.*;

public class deleteSupplier {

    private JPanel mainPanel;
    private JButton deleteButton;
    private JButton cancelButton;

    private supplier selectedSupplier;
    private Runnable refresh;


    public deleteSupplier(supplier supplier, Runnable refresh){

        this.selectedSupplier = supplier;
        this.refresh = refresh;


        deleteButton.addActionListener(e -> deleteSupplier());


        cancelButton.addActionListener(e -> {

            Window window =
                    SwingUtilities.getWindowAncestor(mainPanel);

            window.dispose();

        });

    }


    private void deleteSupplier(){

        supplierDAO dao = new supplierDAO();


        if(dao.hasBooks(selectedSupplier.getSupplierId())){

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Cannot delete this supplier because it has books."
            );

            return;
        }


        boolean success =
                dao.deleteSupplier(selectedSupplier.getSupplierId());


        if(success){

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Supplier deleted successfully!"
            );


            refresh.run();


            Window window =
                    SwingUtilities.getWindowAncestor(mainPanel);

            window.dispose();


        }else{

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Failed to delete supplier."
            );
        }
    }


    public JPanel getMainPanel(){
        return mainPanel;
    }
}