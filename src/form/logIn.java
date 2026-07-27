package form;

import database.DBConnection;
import model.staff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class logIn {
    private JPanel mainPanel;
    private JPanel LoginCard;
    private JButton logInButton;
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JLabel storeLogo;
    private JLabel failMsg;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public logIn() {

        failMsg.setVisible(false);
        failMsg.setForeground(new Color(0xD2691E));
        failMsg.setText("Invalid username or password");

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Connection con = DBConnection.getConnection();

                if (con != null) {

                    String sql = "SELECT * FROM staff WHERE username = ? AND password = ?";

                    try (PreparedStatement ppst = con.prepareStatement(sql)) {

                        ppst.setString(1, usernameInput.getText());
                        ppst.setString(2, new String(passwordInput.getPassword()));

                        ResultSet rs = ppst.executeQuery();

                        if (rs.next()) {
                            staff loggedInStaff = new staff(
                                    rs.getInt("staff_id"),
                                    rs.getString("username"),
                                    rs.getString("password"),
                                    rs.getString("full_name"),
                                    rs.getString("role")
                            );

                            if (loggedInStaff.getRole().equalsIgnoreCase("Admin")) {

                                openDashboard(
                                        new adminDashboard(loggedInStaff).getMainPanel(),
                                        "Admin Dashboard"
                                );

                            } else if (loggedInStaff.getRole().equalsIgnoreCase("Cashier")) {

                                JOptionPane.showMessageDialog(null, "Cashier login successful!");

                                openDashboard(
                                        new cashierDashboard().getMainPanel(),
                                        "Cashier Dashboard"
                                );
                            }

                        } else {
                            failMsg.setVisible(true);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                                "Database Error: " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void openDashboard(JPanel panel, String title) {

        JFrame frame = new JFrame(title);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Window window = SwingUtilities.getWindowAncestor(mainPanel);
        if (window != null) {
            window.dispose();
        }
    }
}