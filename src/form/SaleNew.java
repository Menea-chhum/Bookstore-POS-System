package form;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaleNew {

    private JPanel MainPanel;
    private JLabel StoreLogo;
    private JLabel AvatarLabel;
    private JButton button1;
    private JPanel header;
    private JPanel Newsale;
    private JTextField textField1;
    private JButton a︎Button;
    private JTextField txtSeach;
    private JButton btnSearch;
    private JSpinner spinner1;
    private JButton addToCartButton;
    private JTable table1;
    private JButton button2;
    private JTable table2;
    private JButton confirmButton;
    private JButton cancelButton;
    private JButton logOutButton;

    public SaleNew() {

        // Quantity Spinner
        spinner1.setValue(1);

        // Search Button
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String keyword = txtSeach.getText();

                if(keyword.isEmpty()){
                    JOptionPane.showMessageDialog(null,
                            "Please enter a book name.");
                }else{
                    JOptionPane.showMessageDialog(null,
                            "Searching: " + keyword);
                }

            }
        });

        // Add to Cart
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DefaultTableModel model =
                        (DefaultTableModel) table1.getModel();

                model.addRow(new Object[]{
                        "Book Name",
                        spinner1.getValue(),
                        10.00
                });

            }
        });

        // Confirm Sale
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(null,
                        "Sale completed successfully.");

            }
        });

        // Cancel Sale
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                txtSeach.setText("");
                spinner1.setValue(1);

                DefaultTableModel model =
                        (DefaultTableModel) table1.getModel();

                model.setRowCount(0);

            }
        });

        // Logout
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int result = JOptionPane.showConfirmDialog(
                        null,
                        "Do you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION
                );

                if(result == JOptionPane.YES_OPTION){

                    JFrame frame = (JFrame)
                            SwingUtilities.getWindowAncestor(MainPanel);

                    frame.dispose();

                    JFrame login = new JFrame("Login");
                    login.setContentPane(new logIn().getMainPanel());
                    login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    login.pack();
                    login.setLocationRelativeTo(null);
                    login.setVisible(true);
                }

            }
        });

    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}