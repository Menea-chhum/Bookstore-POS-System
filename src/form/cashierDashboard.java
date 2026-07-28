package form;

import model.staff;

import javax.swing.*;

public class cashierDashboard {

    private JPanel MainPanel;
    private JPanel StaffCard;
    private JButton newSaleButton;
    private JButton logoutButton;

    public cashierDashboard(staff loggedInStaff) {

        // Open New Sale Form
        newSaleButton.addActionListener(e -> {

            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(MainPanel);
            currentFrame.dispose();

            JFrame frame = new JFrame("New Sale");
            frame.setContentPane(new NewSale().getMainPanel());   // Change to newSale() if your class is named newSale
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        // Logout
        logoutButton.addActionListener(e -> {

            int option = JOptionPane.showConfirmDialog(
                    MainPanel,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(MainPanel);
                currentFrame.dispose();

                JFrame loginFrame = new JFrame("Bookstore Login");
                loginFrame.setContentPane(new logIn().getMainPanel());
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.pack();
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);
            }
        });
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }
}