package form;

import DAO.categoryDAO;
import model.category;

import javax.swing.*;
import java.awt.event.*;

public class editCategory extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonSaveChange;
    private JTextField categoryNameField;
    private JTextField descriptionField;
    private category currentCategory;
    private Runnable refreshCallback;

    public editCategory(category c, Runnable refreshCallback) {

        this.currentCategory = c;
        this.refreshCallback = refreshCallback;

        setContentPane(contentPane);
        setModal(true);

        // show existing data
        categoryNameField.setText(c.getCategoryName());
        descriptionField.setText(c.getDescription());

        buttonCancel.addActionListener(e -> dispose());
        buttonSaveChange.addActionListener(e -> onSaveChange());
    }

//    public editCategory() {
//        setContentPane(contentPane);
//        setModal(true);
//        getRootPane().setDefaultButton(buttonCancel);
//
//        buttonCancel.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {onCancel();
//            }
//        });
//
//        buttonSaveChange.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                onSaveChange();
//            }
//        });
//
//        // call onCancel() when cross is clicked
//        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//        addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                onCancel();
//            }
//        });
//
//        // call onCancel() on ESCAPE
//        contentPane.registerKeyboardAction(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                onCancel();
//            }
//        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
//    }

    private void onCancel() {
        // add your code here
        dispose();
    }

    private void onSaveChange() {

        currentCategory.setCategoryName(
                categoryNameField.getText().trim()
        );

        currentCategory.setDescription(
                descriptionField.getText().trim()
        );

        categoryDAO dao = new categoryDAO();

        if (dao.updateCategory(currentCategory)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Category updated successfully."
            );

            dispose();              // Close first

            refreshCallback.run();  // Then refresh dashboard

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update category."
            );
        }
    }

}
