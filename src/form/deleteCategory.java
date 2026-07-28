package form;

import DAO.categoryDAO;
import model.category;

import javax.swing.*;
import java.awt.*;

public class deleteCategory {

    private JPanel mainPanel;
    private JButton deleteButton;
    private JButton cancelButton;

    private category currentCategory;
    private Runnable refreshCallback;

    public deleteCategory(category c, Runnable refreshCallback) {

        this.currentCategory = c;
        this.refreshCallback = refreshCallback;

        deleteButton.addActionListener(e -> deleteCategory());

        cancelButton.addActionListener(e ->
                SwingUtilities.getWindowAncestor(mainPanel).dispose());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
    private void deleteCategory() {

        categoryDAO dao = new categoryDAO();

        if (dao.hasBooks(currentCategory.getCategoryId())) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "This category contains books and cannot be deleted."
            );

            return;
        }

        if (dao.deleteCategory(currentCategory.getCategoryId())) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Category deleted successfully."
            );

            refreshCallback.run();

            Window window = SwingUtilities.getWindowAncestor(mainPanel);

            if (window != null) {
                window.dispose();      // close first
            }

            refreshCallback.run();

        } else {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Unable to delete category."
            );
        }
    }
}