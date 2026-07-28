package form;

import DAO.categoryDAO;
import model.category;
import javax.swing.table.DefaultTableModel;

import javax.swing.*;
import java.awt.*;

public class addCategoryForm {
    private JPanel mainPanel;
    private JButton clearAllButton;
    private JButton a️SaveAllCategoriesButton;
    private JTable table1;
    private JTextField categoryNameInput;
    private JTextField descriptionInput;
    private JButton addToListButton;
    private JButton deleteButton;
    private DefaultTableModel model;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public addCategoryForm(Runnable refreshCallback) {

        model = new DefaultTableModel(
                new Object[]{"Category Name", "Description"}, 0
        );

        table1.setModel(model);
        addToListButton.addActionListener(e -> {

            String name = categoryNameInput.getText().trim();
            String description = descriptionInput.getText().trim();

            if(name.isEmpty()){

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Category name is required."
                );

                return;
            }

            model.addRow(new Object[]{
                    name,
                    description
            });

            categoryNameInput.setText("");
            descriptionInput.setText("");

        });
        a️SaveAllCategoriesButton.addActionListener(e -> {

            categoryDAO dao = new categoryDAO();

            for(int i = 0; i < model.getRowCount(); i++){

                String name = model.getValueAt(i,0).toString();

                String description =
                        model.getValueAt(i,1).toString();

                category c = new category(
                        0,
                        name,
                        description
                );

                dao.addCategory(c);

            }

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Categories added successfully."
            );

            refreshCallback.run();

            SwingUtilities.getWindowAncestor(mainPanel).dispose();

        });


    }
}
